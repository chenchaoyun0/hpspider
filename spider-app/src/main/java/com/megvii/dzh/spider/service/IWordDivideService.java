package com.megvii.dzh.spider.service;

import com.megvii.dzh.spider.common.enums.WordDivideType;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.domain.vo.NameValue;
import java.util.List;

public interface IWordDivideService extends IBaseService<WordDivide> {

  /**
   * 热点词汇
   */
  List<NameValue> nameValues(WordDivideType type, long limit);

}
