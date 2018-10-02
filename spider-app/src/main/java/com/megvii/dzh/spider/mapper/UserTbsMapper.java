package com.megvii.dzh.spider.mapper;

import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.vo.NameValue;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface UserTbsMapper extends Mapper<UserTbs>{

    @Select("select tb_level as name,count(1) as value from user_tbs where tb_name=#{0} GROUP BY tb_level")
    List<NameValue> getUserLevel(String tbName);

    @Select("select tb_name as name,count(1) as value from user_tbs where tb_name!=#{arg1} GROUP BY tb_name ORDER BY value desc LIMIT #{arg0}")
    List<NameValue> getTbNameWordCloud(int limit,String tbName);
}