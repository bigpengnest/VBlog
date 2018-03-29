package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.ViewObject;
import com.BigPeng.VBlog.model.*;
import com.BigPeng.VBlog.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.MethodWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @RequestMapping(path = {"/home"})
    public String home(Model model,
                       HttpServletRequest request){
        User user = new User();
        if (hostHolder.getUser()!=null) {
            user = hostHolder.getUser();
        }else {
            return "redirect:/";
        }
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
    }

    @RequestMapping(path = {"/user/{userId}"})
    public String getUser(Model model,
                          @PathVariable("userId") int userId,
                          HttpServletRequest request){
        User user = new User();
        User viewUser = new User();
        if (hostHolder.getUser()!=null) {
            user = hostHolder.getUser();
        }else {
            return "redirect:/";
        }
        viewUser = userService.selectById(userId);
        List<Blog> list = blogService.getBlogList(viewUser.getId(), 0, 6);
        boolean follow = followService.isFollower(user.getId(),EntityType.ENTITY_USER,userId);
        List<ViewObject> blogs = new LinkedList<>();

        List<ViewObject> messages= new LinkedList<>();
        List<Message> messageList = messageService.getLatestMessage(viewUser.getId(),0,3);
        int index=0;
        if (user.getId() == userId) {
            for (Message message:messageList){
                ViewObject vo = new ViewObject();
                vo.set("fromUser",userService.selectById(message.getFromId()).getName());
                vo.set("content",message.getContent());
                vo.set("index",index++);
                messages.add(vo);
            }
        }else {
            messages =null;
        }
        for (Blog blog:list){
            ViewObject vo = new ViewObject();
            int count = commentService.getCommentCount(blog.getBlogId(),EntityType.ENTITY_BLOG);
            vo.set("count",count);
            vo.set("blog",blog);
            blogs.add(vo);
        }
        model.addAttribute("messages",messages);
        model.addAttribute("blogs", blogs);
        model.addAttribute("user", user);
        model.addAttribute("viewUser",viewUser);
        model.addAttribute("follow",follow);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}/blog"})
    public String blog(Model model,
                       @PathVariable("userId") int userId,
                       @RequestParam(required=true,defaultValue="1")Integer page,
                       HttpServletRequest request) {
        User user = new User();
        User viewUser = new User();
        if (hostHolder.getUser()!=null) {
            user = hostHolder.getUser();
        }else {
            return "redirect:/";
        }
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

    }
    @RequestMapping(path = {"/user/{userId}/blog/trans"})
    public String transpage(@PathVariable("userId") int userId,
                            HttpServletRequest request){
        String page = request.getParameter("transpage");
        return "redirect:/user/"+userId+"/blog?page="+page;
    }

}
