package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.HostHolder;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.UserService;
import com.BigPeng.VBlog.util.VBlogUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class BlogController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BlogController.class);

    @Autowired
    BlogService blogService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserService userService;

    @RequestMapping("/editor")
    public String showPage(Model model){
        Blog blog = new Blog();
        model.addAttribute("blog",blog);
        return "editor";
    }

    @RequestMapping(path = {"/blog/add"})
    public String addBlog(Model model,
                          @ModelAttribute(value = "blog")Blog blog,
                          HttpServletRequest request){
        String ticket = null;
        String content = request.getParameter("myContent");
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

        try {
            blog.setCommentCount(0);
            blog.setCreatedDate(new Date());
            blog.setUserId(user.getId());
            blog.setContent(content);
            blog.setImgsrc(VBlogUtil.getImgSrc(content));
            System.out.println(blog.getImgsrc());
            if (blogService.addBlog(blog) > 0) {
                model.addAttribute("user",user);
                return "redirect:/home";
            }
            System.out.println(blog.getUserId());
        }catch (Exception e) {
            logger.error("添加博客失败"+e.getMessage());
        }
        model.addAttribute("msg","提交失败");
        return "/blog/add";
    }

    @RequestMapping("/blog/{blogId}")
    public String blogDetail(Model model,
                             @PathVariable("blogId") int blogId){
        Blog blog = blogService.getBlogById(blogId);
        User user = userService.selectById(blog.getUserId());
        model.addAttribute("blog",blog);
        model.addAttribute("user",user);
        return "detail";
    }
    @RequestMapping("/detail")
    public String single(){
        return "detail";
    }
}
