package com.megvii.dzh.spider.service.impl;

import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.mapper.UserMapper;
import com.megvii.dzh.spider.service.IUserService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public List<NameValue> getUserLevel() {
    try {
      List<NameValue> list=new ArrayList<>();
      //<20级
      Example ex1 = new Example(User.class);
      ex1.createCriteria().andLessThan("level", 20);
      int rs1 = userMapper.selectCountByExample(ex1);
      list.add(new NameValue("<20级",rs1));
      //20~50
      Example ex2 = new Example(User.class);
      ex2.createCriteria().andBetween("level",20,49);
      int rs2 = userMapper.selectCountByExample(ex2);
      list.add(new NameValue("20~50级",rs2));
      //50~80
      Example ex3 = new Example(User.class);
      ex3.createCriteria().andBetween("level",50,79);
      int rs3 = userMapper.selectCountByExample(ex2);
      list.add(new NameValue("50~80级",rs3));
      //80~100
      Example ex4 = new Example(User.class);
      ex4.createCriteria().andBetween("level",80,99);
      int rs4 = userMapper.selectCountByExample(ex2);
      list.add(new NameValue("80~100级",rs4));
      //>100
      Example ex5 = new Example(User.class);
      ex5.createCriteria().andGreaterThanOrEqualTo("level",100);
      int rs5 = userMapper.selectCountByExample(ex2);
      list.add(new NameValue(">100级",rs5));
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getUserLevel error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getAffiliationPie() {
    try {
      List<NameValue> list = userMapper.getAffiliationPie();
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getAffiliationPie error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getUsertbAge(int limit) {
    try {
      List<NameValue> list = userMapper.getUsertbAge(limit);
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getUsertbAge error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getUserFansBar(int limit) {
    try {
      List<NameValue> list = null;
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getUserLevel error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getUserGender() {
    try {
      List<NameValue> list = userMapper.getUserGender();
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getUserGender error {}", e);
    }
    return null;
  }


}
