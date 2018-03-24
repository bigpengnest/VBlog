package com.BigPeng.VBlog.service;

import com.BigPeng.VBlog.dao.BlogDao;
import com.BigPeng.VBlog.model.Blog;
import com.BigPeng.VBlog.util.VBlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

@Service
public class BlogService {
    @Autowired
    BlogDao blogDao;

    public int addBlog(Blog blog){

        return blogDao.addBlog(blog);
    }

    public List<Blog> getBlogList(int userId,int offset,int limit){
        List<Blog> list = blogDao.getBlogList(userId,offset,limit);
        for (Blog blog:list){
            String content = VBlogUtil.StripHTML(blog.getContent());
            blog.setContent(content);
        }
        return list;
    }

    public Blog getBlogById(int blogId){
        return blogDao.getBlogById(blogId);
    }
}
