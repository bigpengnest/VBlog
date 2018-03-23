package com.BigPeng.VBlog.controller;

import com.BigPeng.VBlog.dao.LoginTicketDao;
import com.BigPeng.VBlog.model.*;
import com.BigPeng.VBlog.service.CommentService;
import com.BigPeng.VBlog.service.UserService;
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
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/blog/{blogId}/addComment"})
    public String addComment(@PathVariable("blogId") int blogId,
                           HttpServletRequest request){
        String ticket = null;
        String commnet = request.getParameter("commet_content");
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
                return "redirect:/";
            user = userService.selectById(loginTicket.getUserId());
        }
        Comment comment = new Comment();
        comment.setContent(commnet);
        comment.setCreatedDate(new Date());
        comment.setUserId(user.getId());
        comment.setEntityId(blogId);
        comment.setEntityType(EntityType.ENTITY_BLOG);
        comment.setStatus(0);
        int count = commentService.addComment(comment);
        return "redirect:/blog/"+blogId;
    }
}
