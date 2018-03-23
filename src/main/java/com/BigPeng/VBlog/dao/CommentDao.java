package com.BigPeng.VBlog.dao;

import com.BigPeng.VBlog.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type,created_date,status ";
    String SELECT_FIELDS = " comment_id, " + INSERT_FIELDS;

    @Insert({"insert into "+ TABLE_NAME+" ( "+SELECT_FIELDS+" ) values (#{commentId},#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where entity_id=#{entityId} and " +
            " entity_Type = #{entityType} order by created_date desc "})
    List<Comment> selectCommentByEntity(@Param("entityId") int userId,
                                        @Param("entityType") int entityType);

    @Select({"select count(comment_id) from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId,
                        @Param("entityType") int entityType);
}
