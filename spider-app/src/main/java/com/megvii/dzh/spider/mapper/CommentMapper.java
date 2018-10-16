package com.megvii.dzh.spider.mapper;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.vo.NameValue;
import tk.mybatis.mapper.common.Mapper;

public interface CommentMapper extends Mapper<Comment> {

  @Select("select user_name as name,count(1) as value from `comment` where year=#{yearBegin} GROUP BY user_name ORDER BY value desc LIMIT #{limit}")
  List<NameValue> getActiveUser(@Param("yearBegin") int yearBegin,@Param("yearEnd") int yearEnd, @Param("limit") int limit);

  @Select("select user_name as name,count(1) as value from `comment` GROUP BY user_name ORDER BY value desc LIMIT #{limit}")
  List<NameValue> getActiveUserBar(@Param("limit") int limit);

  @Select("select if(user_device is null,'未知',user_device) as name,count(1) as value from `comment` GROUP BY user_device ORDER BY value desc LIMIT #{arg0}")
  List<NameValue> getUserDevicePie(int limit);

  @Select("select id from comment order by id desc limit 1")
  int getLastId();

  @Select("SELECT content as name,light_count as value from `comment` WHERE `year`=#{year} ORDER BY light_count DESC LIMIT 30;")
  List<NameValue> getReplyLightYear(int year);

  @Select("SELECT ${groupBy} as name,count(1) as value from comment GROUP BY ${groupBy} ORDER BY ${groupBy}")
  List<NameValue> getCommentGroupBy(@Param("groupBy") String groupBy);

}
