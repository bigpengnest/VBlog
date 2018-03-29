package com.BigPeng.VBlog.model;

import java.util.Date;

public class Message {
    private int messageId;
    //发送者
    private int fromId;
    //消息接受者
    private int toId;
    //消息内容
    private String content;
    //时间
    private Date createdDate;
    //是否
    private int hasRead;

    private String conversationId;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getHasRead() {
        return hasRead;
    }

    public void setHasRead(int hasRead) {
        this.hasRead = hasRead;
    }

    public String getConversationId() {
        if(fromId < toId)
            return String.format("%d_%d",fromId,toId);
        else
            return String.format("%d_%d",toId,fromId);
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
