package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.UserService;
import com.BigPeng.VBlog.util.VBlogUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @RequestMapping("/")
    public String index(){
        return "login";
    }

    @RequestMapping(path = {"/login"})
    @ResponseBody
    public String login(String username, String password,
                        HttpServletResponse response){

        Map<String,String> map = userService.login(username,password);
        Cookie cookie = new Cookie("ticket",map.get("ticket"));
        response.addCookie(cookie);
        return VBlogUtil.getJSONString(map);
    }

    @RequestMapping(path = {"/checkemail"})
    @ResponseBody
    public String checkemail(String email){
        User user = userService.selectByEmail(email);
        JSONObject json = new JSONObject();
        if (user!=null){
            json.put("status",1);
        }else {
            json.put("status",0);
        }
        return json.toJSONString();
    }

    @RequestMapping(path = {"/checkname"})
    @ResponseBody
    public String checkename(String username){
        User user = userService.selectByName(username);
        JSONObject json = new JSONObject();
        if (user!=null){
            json.put("status",1);
        }else {
            json.put("status",0);
        }
        return json.toJSONString();
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @RequestMapping(path = {"/reg"})
    @ResponseBody
    public String regUser(String username,
                          String password,
                          String email){
        if (username==null ||password==null||email==null){
            return VBlogUtil.getJSONString(1);
        }
        boolean status = userService.register(username,password,email);
        return VBlogUtil.getJSONString(status);
    }
}
