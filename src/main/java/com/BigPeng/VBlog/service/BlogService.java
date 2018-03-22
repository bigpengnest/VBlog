package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.dao.BlogDao;
import com.BigPeng.VBlog.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlogService {
    @Autowired
    BlogDao blogDao;

    public int addBlog(Blog blog){

        return blogDao.addBlog(blog);
    }


}
