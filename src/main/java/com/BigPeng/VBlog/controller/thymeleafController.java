package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class thymeleafController {

    @RequestMapping("/test")
    public String test(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "test";
    }

}
