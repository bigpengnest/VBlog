package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @RequestMapping(path = {"/home"})
    public String home(Model model,
                       HttpServletRequest request){
        User user = new User();
        String ticket = null;
        if(request.getCookies()!=null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket != null){
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return "login";
            user = userService.selectById(loginTicket.getUserId());
            System.out.println(user.getId());
            List<Blog> list = blogService.getBlogList(user.getId(),0,6);
            System.out.println(list.size());
            for (Blog blog:list){
                System.out.println(blog.getBlogId());
            }
            model.addAttribute("list",list);
            model.addAttribute("user",user);
            return "home";
        }else {
            model.addAttribute("user",user);
            return "login";
        }

    }
}
