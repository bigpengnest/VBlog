package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.ViewObject;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.EntityType;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.CommentService;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = {"/home"})
    public String home(Model model,
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
                return "redirect:/";
            user = userService.selectById(loginTicket.getUserId());
            List<Blog> list = blogService.getBlogList(user.getId(),0,5);
            List<ViewObject> blogs = new LinkedList<>();
            System.out.println(list.size());
            for (Blog blog:list){
                ViewObject vo = new ViewObject();
                vo.set("count",commentService.getCommentCount(blog.getBlogId(), EntityType.ENTITY_BLOG));
                vo.set("blog",blog);
                blogs.add(vo);
            }
            model.addAttribute("blogs",blogs);
            model.addAttribute("user",user);
            return "home";
        }else {
            model.addAttribute("user",user);
            return "redirect:/";
        }


    }


    @RequestMapping(path = {"/{userId}/home"})
    public String viewUserhome(Model model,
                       @PathVariable("userId") int userId,
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
                return "redirect:/";
            user = userService.selectById(userId);
            List<Blog> list = blogService.getBlogList(userId,0,5);
            List<ViewObject> blogs = new LinkedList<>();
            System.out.println(list.size());
            for (Blog blog:list){
                ViewObject vo = new ViewObject();
                vo.set("count",commentService.getCommentCount(blog.getBlogId(), EntityType.ENTITY_BLOG));
                vo.set("blog",blog);
                blogs.add(vo);
            }
            model.addAttribute("blogs",blogs);
            model.addAttribute("user",user);
            return "home";
        }else {
            model.addAttribute("user",user);
            return "redirect:/";
        }
    }

    @RequestMapping(path = {"/user/{userId}"})
    public String getUser(Model model,
                          @PathVariable("userId") int userId,
                          HttpServletRequest request){
        User user = new User();
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return "redirect:/";
            user = userService.selectById(userId);
            System.out.println(user.getId());
            List<Blog> list = blogService.getBlogList(userId, 0, 10);
            List<ViewObject> blogs = new LinkedList<>();
            for (Blog blog:list){
                ViewObject vo = new ViewObject();
                int count = commentService.getCommentCount(blog.getBlogId(),EntityType.ENTITY_BLOG);
                vo.set("count",count);
                vo.set("blog",blog);
                blogs.add(vo);
            }
            model.addAttribute("blogs", blogs);
            model.addAttribute("user", user);
            return "home";
        } else {
            model.addAttribute("user", user);
            return "redirect:/";
        }
    }

    @RequestMapping(path = {"/{userId}/blog"})
    public String blog(Model model,
                       @PathVariable("userId") int userId,
                       HttpServletRequest request) {
        User user = new User();
        String ticket = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                return "login";
            user = userService.selectById(userId);
            System.out.println(user.getId());
            List<Blog> list = blogService.getBlogList(userId, 0, 10);
            List<ViewObject> blogs = new LinkedList<>();
            for (Blog blog:list){
                ViewObject vo = new ViewObject();
                int count = commentService.getCommentCount(blog.getBlogId(),EntityType.ENTITY_BLOG);
                vo.set("count",count);
                vo.set("blog",blog);
                blogs.add(vo);
            }
            model.addAttribute("blogs", blogs);
            model.addAttribute("user", user);
            return "blog";
        } else {
            model.addAttribute("user", user);
            return "redirect:/";
        }
    }
}
