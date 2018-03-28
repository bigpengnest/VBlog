package com.BigPeng.VBlog.dao;

import com.BigPeng.VBlog.model.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogDao {
    String TABLE_NAME = " blog ";
    String INSERT_FIELDS = " blog_id,blog_title,content,user_id,created_date,comment_count,classify,imgsrc ";
    String SELECT_FIELDS = " blog_id, " + INSERT_FIELDS;

    @Insert({"insert into "+ TABLE_NAME+" ( "+INSERT_FIELDS+" ) values (#{blogId},#{blogTitle},#{content},#{userId},#{createdDate},#{commentCount},#{classify},#{imgsrc})"})
    int addBlog(Blog blog);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where user_id=#{userid} order by created_date desc limit #{offset},#{limit}"})
    List<Blog> getBlogList(@Param("userid") int userId,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where blog_id=#{blogId}"})
    Blog getBlogById(int blogId);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where user_id=#{userId} order by created_date desc"})
    List<Blog> getBlogListByUserId(@Param("userId") int userId);

    @Select({"select count(blog_id) from ",TABLE_NAME," where user_id=#{userId}"})
    int getBlogCount(@Param("userId") int userId);


}
