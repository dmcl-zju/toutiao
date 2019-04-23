package com.zju.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zju.pojo.LoginTicket;

public interface LoginTicketMapper {
	
	String TABLE_NAME = " login_ticket ";
	String INSERT_FIELDS = " user_id,ticket,expired,status ";
	String SELECT_FIELDS = " id,user_id userId,ticket,expired,status ";
	
	//获取所有的ticket
	@Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where ticket=#{ticket}"})
	LoginTicket selByTicket(String ticket);
	
	//插入一个ticket
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")","values(#{userId},#{ticket},#{expired},#{status})"})
	int insLoginTicket(LoginTicket ticket);
	
	//更新一个ticket的status属性
	@Update({"update",TABLE_NAME,"set status=#{status} where ticket=#{ticket}"})
	int updLoignTicket(@Param("ticket") String ticket, @Param("status") int status);
}
