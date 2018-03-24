package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.ViewObject;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;

@Controller
public class thymeleafController {

    @Autowired
    BlogService blogService;

    @RequestMapping("/test")
    public String test(Model model){
        List<Blog> bloglist = blogService.getBlogList(6,0,4);
        ViewObject vo = new ViewObject();
        int i = 1;
        List<ViewObject> blogs = new LinkedList<>();
        for (Blog blog :bloglist){
            vo.set("index",i++);
            vo.set("blog",blog);
            blogs.add(vo);
        }
        model.addAttribute("blogs",blogs);
        return "test";
    }

}
