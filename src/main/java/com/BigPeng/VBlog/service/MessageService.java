package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.dao.MessageDao;
import com.BigPeng.VBlog.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;


    public int addMessage(Message message){
        message.setContent(message.getContent());
        return messageDao.addComment(message) >0 ?message.getMessageId() : 0;
    }

    public List<Message> getConversationDetail(String conversetionId, int offset, int limit){
        return messageDao.getConversationDetail(conversetionId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDao.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }

    public int updateHasRead(String conversationId){
        return messageDao.updateHasRead(conversationId);
    }

    public List<Message> getLatestMessage(int userId,int offset,int limit){
        return messageDao.getLatestMessage(userId,offset,limit);
    }
}

