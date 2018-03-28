package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.ViewObject;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.EntityType;
import com.BigPeng.VBlog.model.LoginTicket;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.CommentService;
import com.BigPeng.VBlog.service.FollowService;
import com.BigPeng.VBlog.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    FollowService followService;

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
            model.addAttribute("viewUser",user);
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
        User viewUser = new User();
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
            user = userService.selectById(loginTicket.getUserId());
            viewUser = userService.selectById(userId);
            List<Blog> list = blogService.getBlogList(viewUser.getId(), 0, 6);
            boolean follow = followService.isFollower(user.getId(),EntityType.ENTITY_USER,userId);
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
            model.addAttribute("viewUser",viewUser);
            model.addAttribute("follow",follow);
            return "home";
        } else {
            model.addAttribute("user", user);
            return "redirect:/";
        }
    }

    @RequestMapping(path = {"/user/{userId}/blog"})
    public String blog(Model model,
                       @PathVariable("userId") int userId,
                       @RequestParam(required=true,defaultValue="1")Integer page,
                       HttpServletRequest request) {
        User user = new User();
        User viewUser = new User();
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
            user = userService.selectById(loginTicket.getUserId());
            viewUser = userService.selectById(userId);
            if (page==0){
                page=1;
            }
            PageHelper.startPage(page, 6);
            List<Blog> list = blogService.getBlogList(viewUser.getId());
            PageInfo<Blog> p=new PageInfo<Blog>(list);

            List<ViewObject> blogs = new LinkedList<>();
            for (Blog blog:list){
                ViewObject vo = new ViewObject();
                int count = commentService.getCommentCount(blog.getBlogId(),EntityType.ENTITY_BLOG);
                vo.set("count",count);
                vo.set("blog",blog);
                blogs.add(vo);
            }
            boolean follow = followService.isFollower(user.getId(),EntityType.ENTITY_USER,userId);
            System.out.println(follow);
            model.addAttribute("page", p);
            model.addAttribute("blogList",list);
            model.addAttribute("viewUser",viewUser);
            model.addAttribute("follow",follow);
            model.addAttribute("blogs", blogs);
            model.addAttribute("user", user);
            return "blog";
        } else {
            model.addAttribute("user", user);
            return "redirect:/";
        }
    }
    @RequestMapping(path = {"/user/{userId}/blog/trans"})
    public String transpage(@PathVariable("userId") int userId,
                            HttpServletRequest request){
        String page = request.getParameter("transpage");
        return "redirect:/user/"+userId+"/blog?page="+page;
    }

}
