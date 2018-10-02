package com.megvii.dzh.spider.service.impl;

import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.mapper.UserMapper;
import com.megvii.dzh.spider.service.IUserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

  @Autowired
  private UserMapper userMapper;

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
      List<NameValue> list = userMapper.getUserFansBar(Constant.getTbName(),limit);
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
