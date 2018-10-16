package com.megvii.dzh.spider.service.impl;

import com.megvii.dzh.spider.common.utils.DateConvertUtils;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.CountGroupByUser;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonth;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;
import com.megvii.dzh.spider.mapper.PostMapper;
import com.megvii.dzh.spider.mapper.UserMapper;
import com.megvii.dzh.spider.service.IPostService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
@Transactional
public class PostServiceImpl extends BaseServiceImpl<Post> implements IPostService {

  @Autowired
  private PostMapper postMapper;

  @Autowired
  private UserMapper userMapper;

  @Override
  public int getLastId(){
    return postMapper.getLastId();
  }


  @Override
  public List<NameValue> getPostUserTopBar(int limit) {
    try {
      List<NameValue> list = postMapper.getPostUserTopBar(limit);
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getUserFansBar error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> nameValuesByYear(String year, long limit) {
    try {
      // 查找年份的帖子
      Example example = new Example(Post.class);
      example.createCriteria()//
          .andEqualTo("year",year)//
          .andGreaterThanOrEqualTo("replyNum",500);
      List<Post> list = postMapper.selectByExample(example);
      log.info("---> size {} data {}", list.size());
      // 分词
      Map<String, Integer> map = new HashMap<>();
      for (Post post : list) {
        String title = post.getTitle();
        if (StringUtils.isBlank(title)) {
          continue;
        }
        List<Word> listWord = WordSegmenter.seg(title);
        for (Word word : listWord) {
          String text = word.getText();
          if (text.trim().matches("\\d++")) {
            continue;
          }
          //
          if (map.containsKey(text)) {
            map.put(text, map.get(text) + 1);
          } else {
            map.put(text, 1);
          }
        }
      }
      List<NameValue> nameValues = new ArrayList<>();
      // for map
      for (Entry<String, Integer> entry : map.entrySet()) {
        nameValues.add(new NameValue(entry.getKey(), entry.getValue()));
      }
      // sort
      Collections.sort(nameValues, new Comparator<NameValue>() {
        @Override
        public int compare(NameValue o1, NameValue o2) {
          return o2.getValue() - o1.getValue();
        }
      });

      if (limit > 0&&nameValues.size()>limit) {
        nameValues = nameValues.subList(0, Integer.parseInt(limit + ""));
      }

      return nameValues;

    } catch (Exception e) {
      log.error("nameValuesByYear error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getPostAndNo() {
    try {
      List<CountGroupByUser> list = postMapper.selectCountGroupByUser();
      // 发帖的用户
      int postCount = list.size();
      log.info("---> postCount {}", postCount);
      // 所有用户
      int userCount = userMapper.selectCount(new User());
      log.info("---> userCount {}", userCount);
      // 不发帖用户
      int noPostCount = userCount - postCount;
      log.info("---> noPostCount {}", noPostCount);
      //
      List<NameValue> result = new ArrayList<>();
      result.add(new NameValue("发过帖的用户", postCount));
      result.add(new NameValue("不发帖用户", noPostCount));

      return result;
    } catch (Exception e) {
      log.error("getPostAndNo error {}", e);
    }

    return null;
  }

  @Override
  public List<NameValue> getPostHasRepAndNo() {
    try {
      int postCount = postMapper.selectCount(new Post());
      log.info("---> postCount {}", postCount);
      Post post = new Post();
      post.setReplyNum(0);
      int postNoRepCount = postMapper.selectCount(post);
      log.info("---> postNoRepCount {}", postNoRepCount);
      int postHasRepCount = postCount - postNoRepCount;
      //
      List<NameValue> result = new ArrayList<>();
      result.add(new NameValue("有回复的帖子", postHasRepCount));
      result.add(new NameValue("没回复的帖子", postNoRepCount));
      return result;
    } catch (Exception e) {
      log.error("getPostHasRepAndNo error {}", e);
    }

    return null;
  }

  @Override
  public List<PostYears> getPostGroupBy(String groupBy) {
    try {
      List<PostYears> list = postMapper.getPostGroupBy(groupBy);
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getPostYears error {}", e);
    }
    return null;
  }

  @Override
  public List<PostGroupByMonthVo> getPostGroupByMonth() {
    try {
      List<PostGroupByMonthVo> list = new ArrayList<>();
      for (int j = 1; j <= 12; j++) {
        PostGroupByMonthVo postGroupByMonthVo = new PostGroupByMonthVo();
        postGroupByMonthVo.setMonth(j);
        //
        PostGroupByMonth groupByMonth = postMapper.getPostGroupByMonth(2014, j);
        postGroupByMonthVo.setCount2014(groupByMonth == null ? 0 : groupByMonth.getCount());
        //
        groupByMonth = postMapper.getPostGroupByMonth(2015, j);
        postGroupByMonthVo.setCount2015(groupByMonth == null ? 0 : groupByMonth.getCount());
        //
        groupByMonth = postMapper.getPostGroupByMonth(2016, j);
        postGroupByMonthVo.setCount2016(groupByMonth == null ? 0 : groupByMonth.getCount());
        //
        groupByMonth = postMapper.getPostGroupByMonth(2017, j);
        postGroupByMonthVo.setCount2017(groupByMonth == null ? 0 : groupByMonth.getCount());
        //
        groupByMonth = postMapper.getPostGroupByMonth(2018, j);
        postGroupByMonthVo.setCount2018(groupByMonth == null ? 0 : groupByMonth.getCount());
        //
        list.add(postGroupByMonthVo);
      }

      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getPostGroupByMonth error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getPostTitlesyear(String year) {
    try {
      List<NameValue> list = postMapper.getPostTitlesyear(year);
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getPostYears error {}", e);
    }
    return null;
  }

  @Override
  public List<NameValue> getPostTitlesyearAll() {
    try {
      List<NameValue> list = postMapper.getPostTitlesyearAll();
      log.info("---> size {} data {}", list.size());
      return list;
    } catch (Exception e) {
      log.error("getPostYears error {}", e);
    }
    return null;
  }
}
