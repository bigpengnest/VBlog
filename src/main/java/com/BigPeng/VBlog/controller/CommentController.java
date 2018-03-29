package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.async.EventHandler;
import com.BigPeng.VBlog.async.EventModel;
import com.BigPeng.VBlog.async.EventProducer;
import com.BigPeng.VBlog.async.EventType;
import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.model.*;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.CommentService;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    BlogService blogService;

    @RequestMapping(path = {"/blog/{blogId}/addComment"})
    public String addComment(@PathVariable("blogId") int blogId,
                           HttpServletRequest request){
        String ticket = null;
        String commnet = request.getParameter("commet_content");
        User user = new User();
        if (hostHolder.getUser()!=null) {
            user = hostHolder.getUser();
        }else {
            return "redirect:/";
        }
        Blog blog = blogService.getBlogById(blogId);
        Comment comment = new Comment();
        comment.setContent(commnet);
        comment.setCreatedDate(new Date());
        comment.setUserId(user.getId());
        comment.setEntityId(blogId);
        comment.setEntityType(EntityType.ENTITY_BLOG);
        comment.setStatus(0);
        int count = commentService.addComment(comment);

        eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                .setActorId(user.getId())
                .setEntityId(blogId)
                .setEntityOwnerId(blog.getUserId()));
        return "redirect:/blog/"+blogId;
    }
}
