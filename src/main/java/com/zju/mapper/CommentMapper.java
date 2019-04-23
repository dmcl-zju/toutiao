package com.zju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zju.pojo.Comment;

public interface CommentMapper {
	
	String TABLE_NAME = " comment ";
	String INSERT_FIELDS = " content,user_id,entity_id,entity_type,created_date,status ";
	String SELECT_FIELDS = " id,content,user_id userId,entity_id entityId,entity_type entityType,created_date createdDate,status ";
	
	//新增一条评论
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")",
			"values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
	int insComment(Comment comment);
	
	//选择指定entityId和entitytype下的所有评论
	@Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
	List<Comment> selByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);
	
	//指定entityId和entitytype下的所有评论数目
	@Select({"select count(id) from",TABLE_NAME,"where entity_id=#{entityId} and entity_type=#{entityType}"})
	int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);
}
