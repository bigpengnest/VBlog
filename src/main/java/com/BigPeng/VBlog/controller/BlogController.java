package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.HostHolder;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.util.VBlogUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class BlogController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    BlogService blogService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping("/editor")
    public String showPage(Model model){
        Blog blog = new Blog();
        model.addAttribute("blog",blog);
        return "editor";
    }

    @RequestMapping("/blog/add")
    public String addBlog(Model model,
                          @ModelAttribute(value = "blog")Blog blog,
                          HttpServletRequest request){
        try {
            String content = request.getParameter("myContent");
            blog.setCommentCount(0);
            blog.setCreatDate(new Date());
            blog.setUserId(1);
            blog.setContent(content);
            if (hostHolder.getUser() == null) {
                blog.setUserId(VBlogUtil.ANONYMOUS_USERID);
            } else {
                blog.setUserId(hostHolder.getUser().getId());
            }
            if (blogService.addBlog(blog) > 0) {
                return "home";
            }
            System.out.println(blog.getUserId());
        }catch (Exception e) {
            logger.error("添加博客失败"+e.getMessage());
        }
        model.addAttribute("msg","提交失败");
        return "/blog/add";

    }
}
