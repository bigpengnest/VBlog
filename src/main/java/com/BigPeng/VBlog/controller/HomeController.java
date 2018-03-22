package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.UserDao;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.security.krb5.internal.Ticket;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class HomeController {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserDao userDao;

    @RequestMapping("/home")
    public String home(Model model,
                       HttpServletRequest request){
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
            User user = userDao.selectById(loginTicket.getUserId());
        }
        User user = new User();
        user.setName("haha");
        model.addAttribute("user",user);
        return "home";
    }
}
