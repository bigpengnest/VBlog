package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.baidu.ueditor.ActionEnter;
import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.MessageEntity;
import com.BigPeng.VBlog.model.HostHolder;
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

    @Autowired
    HostHolder hostHolder;

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
        User user = new User();
        if (hostHolder.getUser()!=null) {
            user = hostHolder.getUser();
        }else {
            return "redirect:/";
        }
        model.addAttribute("user",user);
        return "upimage";
    }

    @RequestMapping(path = {"/uploadImage"})
    public String uploadImage(Model model,
                              @RequestParam(value = "file", required = true) MultipartFile file,
                              HttpServletRequest request) throws IOException {

        User user = new User();
        if (hostHolder.getUser()!=null) {
            user = hostHolder.getUser();
        }else {
            return "redirect:/";
        }
        Map<String,String> map = uploadImageService.uploadImage(file,user);
        model.addAttribute("user",user);
        model.addAttribute("msg",map.get("msg"));
        return "redirect:/setting";
    }
}
