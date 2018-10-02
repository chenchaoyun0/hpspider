package com.megvii.dzh.spider.service.impl;

import com.megvii.dzh.spider.common.enums.WordDivideType;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.mapper.WordDivideMapper;
import com.megvii.dzh.spider.service.IWordDivideService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WordDivideServiceImpl extends BaseServiceImpl<WordDivide> implements
    IWordDivideService {

  @Autowired
  private WordDivideMapper wordDivideMapper;

  @Override
  public List<NameValue> nameValues(WordDivideType type, long limit) {
    try {
      return wordDivideMapper.nameValues(type.getCode(), limit);
    } catch (Exception e) {
      log.error("postTitleWord error {}", e);
    }
    return null;
  }


}
