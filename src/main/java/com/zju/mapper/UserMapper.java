package com.zju.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.zju.pojo.User;

public interface UserMapper {
	String TABLE_NAME = " user ";
	String INSERT_FIELDS = " name,password,salt,head_url ";
	String SELECT_FIELDS = " id,name,password,salt,head_url headurl ";
	@Select({"select",SELECT_FIELDS,"from", TABLE_NAME, "where name=#{name} and password=#{password}"})
	User selByNameAndPassword(User user);
	
	@Select({"select",SELECT_FIELDS,"from", TABLE_NAME, "where name=#{name}"})
	User selByName(String name);
	
	@Select({"select",SELECT_FIELDS,"from", TABLE_NAME, "where id=#{id}"})
	User selById(int id);
	
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")","values(#{name},#{password},#{salt},#{headUrl})"})
	int insUser(User user);
	
	
	
}
