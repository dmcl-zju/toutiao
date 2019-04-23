package com.zju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zju.pojo.Message;

public interface MessageMapper {
	String TABLE_NAME=" message ";
	String INSERT_FIELDS=" from_id,to_id,content,created_date,has_read,conversation_id ";
	String SELECT_FIELDS=" id,from_id fromId,to_id toId,content,created_date createdDate,has_read hasRead,conversation_id conversationId";
	
	//Ϊ��ȡ�б����õ�
	String SELECT_FIELDS2 = "from_id fromId,to_id toId,content,created_date createdDate,has_read hasRead,conversation_id conversationId";
	
	
	//������Ϣ
	@Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")","values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
	int insMessage(Message message);
	
	//��ȡ�Ự����
	@Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
	List<Message> getConversationDetail(@Param("conversationId") String conversationId,@Param("offset") int offset,@Param("limit") int limit);
	
	//��ȡ�Ự�б�--��ÿ���Ự��������Ϣ��ʾ���ѻỰ�б�Ҳ����ʱ����
	//ԭʼsql��䣺select id,conversation_id,count(id) from (select * from message where from_id=15 or to_id=15 order by created_date desc)hah 
	//            GROUP BY conversation_id ORDER BY created_date desc limit 0,4
	@Select({"select",SELECT_FIELDS2,",count(id) id from (select * from message where from_id=#{userId} or to_id=#{userId} "
			+ "order by created_date desc) tt group by conversation_id order by created_date desc limit #{offset},#{limit}"})
	List<Message> getConversationList(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
	
	//��ȡÿ���Ựδ����Ϣ
	@Select({"select count(id) from",TABLE_NAME,"where to_id=#{userId} and has_read=0 and conversation_id=#{conversationId}"})
	int getConversationUnreadCount(@Param("userId") int userId,@Param("conversationId") String conversationId);
	
}
