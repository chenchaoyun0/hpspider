package com.megvii.dzh.spider.service;

import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;
import java.util.List;

public interface IPostService extends IBaseService<Post> {

  /**
   * 热点词汇 按年
   */
  List<NameValue> nameValuesByYear(String year, long limit);

  /**
   * 发帖用户与不发帖用户比
   */
  List<NameValue> getPostAndNo();

  List<NameValue> getPostHasRepAndNo();

  List<PostYears> getPostGroupBy(String groupBy);

  List<PostGroupByMonthVo> getPostGroupByMonth();

  List<NameValue> getPostTitlesyear(String year);

  List<NameValue> getPostUserTopBar(int limit);

}
