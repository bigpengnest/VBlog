package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.dao.CommentDao;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    public List<Comment> selectCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }
}
