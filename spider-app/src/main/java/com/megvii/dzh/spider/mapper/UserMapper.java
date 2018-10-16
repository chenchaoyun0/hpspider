package com.megvii.dzh.spider.mapper;

import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.UserProvinceBarVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

  List<NameValue> getUserFansBar(@Param("limit") int limit, @Param("gender") int gender);

  @Select("select if(gender=1,'男',if(gender=0,'女','未知')) as name,count(1) as value from `user` GROUP BY gender;")
  List<NameValue> getUserGender();

  @Select("select ROUND(tb_age,0) as name,count(1) as value from `user` GROUP BY name ORDER BY value desc LIMIT #{arg0};")
  List<NameValue> getUsertbAge(int limit);

  @Select("SELECT affiliation AS name,count(1) as value from `user` GROUP BY affiliation")
  List<NameValue> getAffiliationPie();

  @Select("select id from user order by id desc limit 1")
  int getLastId();

  @Select("SELECT id,province FROM `user`")
  List<User> selectProvinces();

  @Select("SELECT id,province FROM user")
  List<User> selectForUpdateProvince();

  @Select("SELECT province,COUNT(1) AS totalNum,SUM(gender=1) AS maleNum,SUM(gender=0) AS femaleNum,SUM(gender=-1) AS unknownNum FROM `user` where province!='地球' GROUP BY province ORDER BY totalNum DESC LIMIT #{arg0}")
  List<UserProvinceBarVo> getUserProvinceBar(int limit);

  List<NameValue> getUserViewTotal(@Param("limit") int limit, @Param("gender") int gender);

  @Select("SELECT a.user_name as name,a.sheq_sw as value from (SELECT id,user_name,sheq_sw  FROM `user` ORDER BY sheq_sw ${arg1} LIMIT #{arg0}) a GROUP BY a.user_name ORDER BY a.sheq_sw ${arg1}")
  List<NameValue> getUserSheqSw(int limit, String orderBy);

  @Select("SELECT DATE_FORMAT(join_date,'%Y') as name,count(1) as value from `user` GROUP BY name")
  List<NameValue> getUserJoinYears();

}