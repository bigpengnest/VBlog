package com.BigPeng.VBlog.dao;

import com.BigPeng.VBlog.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url, email, signature ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id=#{id}"})
    User selelctById(int id);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where name=#{username}"})
    User selectByName(String username);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where email=#{email}"})
    User selectByEmail(String email);

    @Insert({"insert into "+ TABLE_NAME+" ( "+INSERT_FIELDS+" ) values (#{name},#{password},#{salt},#{headUrl},#{email},#{signature})"})
    int addUser(User user);

    @Update({"update "+ TABLE_NAME+" set head_url=#{headUrl} where id =#{userId}"})
    int updateHeadUrl(@Param("headUrl") String headUrl,
                      @Param("userId")int userId);
}
