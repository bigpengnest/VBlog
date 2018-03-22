package com.BigPeng.VBlog.dao;

import com.BigPeng.VBlog.model.Blog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogDao {
    String TABLE_NAME = " blog ";
    String INSERT_FIELDS = " blog_id,blog_title,content,user_id,created_date,comment_count,classify ";
    String SELECT_FIELDS = " blog_id, " + INSERT_FIELDS;

    @Insert({"insert into "+ TABLE_NAME+" ( "+INSERT_FIELDS+" ) values (#{id},#{title},#{content},#{userId},#{creatDate},#{commentCount},#{classify})"})
    int addBlog(Blog blog);
}
