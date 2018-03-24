package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.UserDao;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.util.VBlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    public User selectById(int id){
        return userDao.selelctById(id);
    }

    public User selectByName(String username){
        return userDao.selectByName(username);
    }

    public User selectByEmail(String email){
        return userDao.selectByEmail(email);
    }

    public Map<String,String> register(String username,String password,String email){
        Map<String,String> map = new HashMap<>();

        if (StringUtils.isEmpty(email)){
            map.put("msg","邮箱不能为空！");
            return map;
        }

        if (StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if (StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空！");
            return map;
        }
        User user = userDao.selectByEmail(email);

        if (user!=null){
            map.put("msg","邮箱已经被注册！");
            return map;
        }
        user = userDao.selectByName(username);
        if (user!=null){
            map.put("msg","用户名已存在！");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl("\\ueditor\\jsp\\upload\\image\\default\\headimg.jpg");
        user.setPassword(VBlogUtil.MD5(password+user.getSalt()));
        user.setEmail(email);
        user.setSignature(" ");
        userDao.addUser(user);

        return map;
    }

    public Map<String,String> login(String username, String password){
        Map<String,String> map = new HashMap<>();
        User user = selectByName(username);
        if (user ==null){
            map.put("msg","用户名不存在");
            return map;
        }
        if (!user.getPassword().equals(VBlogUtil.MD5(password+user.getSalt()))){
            map.put("msg","密码错误！");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        //设置失效日期
        now.setTime(3600*24*100 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }
}