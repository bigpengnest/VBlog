package com.BigPeng.VBlog.async.handler;

import com.BigPeng.VBlog.async.EventHandler;
import com.BigPeng.VBlog.async.EventModel;
import com.BigPeng.VBlog.async.EventType;
import com.BigPeng.VBlog.model.Message;
import com.BigPeng.VBlog.model.User;
import com.BigPeng.VBlog.service.MessageService;
import com.BigPeng.VBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message= new Message();
        message.setFromId(model.getActorId());
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.selectById(model.getActorId());
        if (model.getEntityType()==0) {
            message.setContent("用户" + user.getName() + "关注了你");
        }else {
            message.setContent("用户" + user.getName() + "取消关注了你");
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
