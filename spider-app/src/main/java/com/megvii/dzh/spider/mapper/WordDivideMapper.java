package com.megvii.dzh.spider.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.domain.vo.NameValue;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface WordDivideMapper  extends Mapper<WordDivide>{

    @Select("select word as name,count(*) as value from word_divide where type=#{type} GROUP BY word ORDER BY value desc LIMIT #{limit};")
    List<NameValue> nameValues(@Param("type") Integer type,@Param("limit")long limit);

    @Select("select id from word_divide order by id desc limit 1")
    int getLastId();
}