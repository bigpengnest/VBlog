package com.BigPeng.VBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PhotoController {

    @RequestMapping("/photo")
    public String photo(){
        return "photos";
    }
}
