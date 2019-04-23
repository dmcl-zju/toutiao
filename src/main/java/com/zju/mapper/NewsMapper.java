package com.zju.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zju.pojo.News;

public interface NewsMapper {
	
	 String TABLE_NAME = " news ";
     String INSERT_FIELDS = " title, link, image, like_count, comment_count,user_id,created_date ";
     String SELECT_FIELDS = " id,title,link,image,like_count likecount,comment_count commentcount,user_id userid,created_date createddate ";
     //����һ������
     @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,")",
    	 	  "values(#{title},#{link},#{image},#{likeCount},#{commentCount},#{userId},#{createdDate})"})
     int insNews(News news);
    
     //��ѯָ�������ߵ�����--�漰����̬sql�����xml�ļ�
     List<News> selNewsByUserIdAndPage(@Param("userId") int userId, @Param("offset") int offset,
    		 						   @Param("limit") int limit);
     
     //����id��ѯ
     @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id = #{id}"})
     News selNewsById(int id);
     
     //����������
     @Update({"update",TABLE_NAME,"set comment_count=#{count} where id=#{newsId}"})
     int updNewsCount(@Param("newsId") int newsId,@Param("count") int count);
     //����ϲ��
     @Update({"update",TABLE_NAME,"set like_count=#{count} where id=#{newsId}"})
     int updLikeCount(@Param("newsId") int newsId,@Param("count") int count);
}
