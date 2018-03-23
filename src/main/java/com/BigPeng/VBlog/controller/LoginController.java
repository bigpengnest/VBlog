package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
    public String index(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "login";
    }

    @RequestMapping(path = {"/login"})
    public String login(Model model,
                        @ModelAttribute(value="user")User user,
                        HttpServletResponse response){
        try {
            Map<String, String> map = userService.login(user.getName(), user.getPassword());
            if (map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);
                List<Blog> list = blogService.getBlogList(userService.selectByName(user.getName()).getId(),0,5);
                model.addAttribute("user",user);
                model.addAttribute("list",list);
                return "home";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e) {
            logger.error("登录异常",e.getMessage());
            return "login";
        }
    }

    @RequestMapping("/register")
    public String register(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }

    @RequestMapping("/reg")
    public String regUser(Model model,
                          @ModelAttribute(value = "user") User user){
        System.out.println(user.getEmail());
        System.out.println(user.getName()+" "+user.getPassword());
        try {
            Map<String,String> map = userService.register(user.getName(),user.getPassword(),user.getEmail());
            model.addAttribute("msg",map.get("msg"));
            System.out.println(map.get("msg"));
            return "login";

        }catch (Exception e){
            logger.error("注册异常:"+e.getMessage());
            return "register";
        }
    }
}
