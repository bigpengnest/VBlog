package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.async.EventModel;
import com.BigPeng.VBlog.async.EventProducer;
import com.BigPeng.VBlog.async.EventType;
import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.dao.ViewObject;
import com.BigPeng.VBlog.model.*;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.CommentService;
import com.BigPeng.VBlog.service.FollowService;
import com.BigPeng.VBlog.service.UserService;
import com.BigPeng.VBlog.util.VBlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/followUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String follow(Model model,
                         @RequestParam("userId") int userId,
                         @RequestParam("followeeId") int followeeId){
        System.out.println(userId+"+"+followeeId);

        boolean ret = followService.follow(userId, EntityType.ENTITY_USER,followeeId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(userId)
                .setEntityId(followeeId)
                .setEntityOwnerId(followeeId).setEntityType(0));

        System.out.println(ret);
        model.addAttribute("follow",true);
        return VBlogUtil.getJSONString(ret ? 0:1,String.valueOf(followService.getFolloweeCount(userId,EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/unfollowUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollow(Model model,
                           @RequestParam("userId") int userId,
                            @RequestParam("followeeId") int followeeId){
        System.out.println(userId+"+"+followeeId);

        boolean ret = followService.unfollow(userId, EntityType.ENTITY_USER,followeeId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(userId)
                .setEntityId(followeeId)
                .setEntityOwnerId(followeeId).setEntityType(1));

        System.out.println(ret);
        model.addAttribute("follow",false);
        return VBlogUtil.getJSONString(ret ? 0:1,String.valueOf(followService.getFolloweeCount(userId,EntityType.ENTITY_USER)));
    }

    @RequestMapping(path = {"/user/{userId}/friends"})
    public String friends(Model model,
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
        int followerCount = (int)followService.getFollowerCount(userId,EntityType.ENTITY_USER);
        List<Integer> followerId = followService.getFollowers(userId,EntityType.ENTITY_USER,followerCount);
        List<User> followers = new ArrayList<>();
        for (int id:followerId){
            followers.add(userService.selectById(id));
        }
        int followeeCount = (int)followService.getFolloweeCount(userId,EntityType.ENTITY_USER);
        List<Integer> followeeId = followService.getFollowees(userId,EntityType.ENTITY_USER,followeeCount);
        List<User> followees = new ArrayList<>();
        for (int id:followeeId){
            followees.add(userService.selectById(id));
        }
        boolean follow = followService.isFollower(user.getId(),EntityType.ENTITY_USER,userId);

        model.addAttribute("followers",followers);
        model.addAttribute("followees",followees);
        model.addAttribute("user", user);
        model.addAttribute("follow",follow);
        model.addAttribute("viewUser", viewUser);

        return "follow";
    }
}
