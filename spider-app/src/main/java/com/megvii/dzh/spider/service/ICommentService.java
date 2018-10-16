package com.megvii.dzh.spider.service;

import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostYears;
import java.util.List;

public interface ICommentService extends IBaseService<Comment> {

  List<NameValue> getActiveUser(int year, int limit);

  List<NameValue> getActiveUserBar(int limit);

  List<NameValue> getUserDevicePie(int limit);

  List<NameValue> getReplyLightYear(int year);

  List<NameValue> getCommentGroupBy(String groupBy);

}
