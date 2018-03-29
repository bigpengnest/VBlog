package com.BigPeng.VBlog.async.handler;

import com.BigPeng.VBlog.async.EventHandler;
import com.BigPeng.VBlog.async.EventModel;
import com.BigPeng.VBlog.async.EventType;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.Message;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.BlogService;
import com.BigPeng.VBlog.service.MessageService;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class CommentHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    MessageService messageService;
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        Blog blog = blogService.getBlogById(model.getEntityId());
        User user = userService.selectById(model.getActorId());
        message.setContent("用户"+user.getName()+"评论了你的博客《"+blog.getBlogTitle()+"》");
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.COMMENT);
    }
}
