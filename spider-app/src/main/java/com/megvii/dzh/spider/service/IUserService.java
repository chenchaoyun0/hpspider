package com.megvii.dzh.spider.service;

import com.github.pagehelper.PageInfo;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.UserProvinceBarVo;
import java.util.List;

public interface IUserService extends IBaseService<User> {

  List<NameValue> getUserFansBar(int limit, int gender);

  List<NameValue> getUserGender();

  List<NameValue> getUsertbAge(int limit);

  List<NameValue> getAffiliationPie();

  List<NameValue> getUserLevel();

  PageInfo<User> getUserPages(int pageNum,int pageSize);

  List<UserProvinceBarVo> getUserProvinceBar(int limit);

  List<NameValue> getUserViewTotal(int limit, int gender);

  List<NameValue> getUserSheqSw(int limit, String orderBy);

  List<NameValue> getUserJoinYears();
}
