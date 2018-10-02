package com.megvii.dzh.spider.mapper;

import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

  @Select(
      "select user_name as name,fans_count as value from `user` where user_name in(SELECT user_name from user_tbs where tb_name=#{arg0}) ORDER BY fans_count desc LIMIT #{arg1};")
  List<NameValue> getUserFansBar(String tbName,int limit);

  @Select("select if(gender=1,'男','女') as name,count(1) as value from `user` GROUP BY gender;")
  List<NameValue> getUserGender();

  @Select("select ROUND(tb_age,0) as name,count(1) as value from `user` GROUP BY name ORDER BY value desc LIMIT #{arg0};")
  List<NameValue> getUsertbAge(int limit);
}