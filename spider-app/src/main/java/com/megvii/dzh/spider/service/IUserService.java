package com.megvii.dzh.spider.service;

import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import java.util.List;

public interface IUserService extends IBaseService<User> {

  List<NameValue> getUserFansBar(int limit);

  List<NameValue> getUserGender();

  List<NameValue> getUsertbAge(int limit);
}
