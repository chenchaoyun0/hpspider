 package com.megvii.dzh.spider.service;

import java.util.List;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.vo.NameValue;

public interface IUserTbsService extends IBaseService<UserTbs> {

    List<NameValue> getUserLevel();

    List<NameValue> getTbNameWordCloud(int limit);

}
