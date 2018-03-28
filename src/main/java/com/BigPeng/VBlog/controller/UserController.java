package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.baidu.ueditor.ActionEnter;
import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.MessageEntity;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.UploadImageService;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UploadImageService uploadImageService;

    @Autowired
    UserService userService;

    @RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(path = {"/upimage"})
    public String upimge(Model model,
                         HttpServletRequest request){
        String ticket = null;

        User user = null;
        if(request.getCookies()!=null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return "login";
            user = userService.selectById(loginTicket.getUserId());
        }
        model.addAttribute("user",user);
        return "upimage";
    }

    @RequestMapping(path = {"/uploadImage"})
    public String uploadImage(Model model,
                              @RequestParam(value = "file", required = true) MultipartFile file,
                              HttpServletRequest request) throws IOException {

        String ticket = null;
        User user = null;
        if(request.getCookies()!=null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if(ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return "login";
            user = userService.selectById(loginTicket.getUserId());
        }
        Map<String,String> map = uploadImageService.uploadImage(file,user);
        model.addAttribute("user",user);
        model.addAttribute("msg",map.get("msg"));
        return "redirect:/setting";
    }
}
