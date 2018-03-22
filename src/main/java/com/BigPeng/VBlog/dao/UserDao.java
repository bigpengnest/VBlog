package com.BigPeng.VBlog.dao;

import com.BigPeng.VBlog.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url, email ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where id=#{id}"})
    User selelctById(int id);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where name=#{username}"})
    User selectByName(String username);

    @Select({"select "+SELECT_FIELDS+" from "+TABLE_NAME+" where email=#{email}"})
    User selectByEmail(String email);

    @Insert({"insert into "+ TABLE_NAME+" ( "+INSERT_FIELDS+" ) values (#{name},#{password},#{salt},#{headUrl},#{email})"})
    int addUser(User user);
}
