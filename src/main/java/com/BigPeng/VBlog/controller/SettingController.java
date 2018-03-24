package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class SettingController {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/setting"})
    public String setting(Model model,
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
            model.addAttribute("user",user);
            return "setting";
        }else {
            model.addAttribute("user",user);
            return "redirect:/";
        }
    }
}
