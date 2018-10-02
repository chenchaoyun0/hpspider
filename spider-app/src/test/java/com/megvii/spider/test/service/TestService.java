package com.megvii.spider.test.service;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.SpiderApplication;
import com.megvii.dzh.spider.common.enums.WordDivideType;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IUserTbsService;
import com.megvii.dzh.spider.service.IWordDivideService;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = {SpiderApplication.class})
@Slf4j
public class TestService {

  @Autowired
  private IPostService postService;
  @Autowired
  private ICommentService commentService;
  @Autowired
  private IUserService userService;
  @Autowired
  private IUserTbsService userTbsService;
  @Autowired
  private IWordDivideService wordDivideService;

  private ExecutorService saveExecutor = Executors
      .newFixedThreadPool(200, new DefaultThreadFactory("saveExecutor"));

  @Test
  public void test() {
    List<NameValue> nameValues = wordDivideService.nameValues(WordDivideType.POST_TITLE, 1000);
    log.info("---> size {} , data {}", nameValues.size(), JSONObject.toJSONString(nameValues));
  }

  @Test
  public void test2() {
    List<NameValue> nameValuesByYear = postService.nameValuesByYear("2016", 1000);
    log.info("---> size {} data {}", nameValuesByYear.size(),
        JSONObject.toJSONString(nameValuesByYear));

  }

  @Test
  public void test3() {
    List<NameValue> postAndNo = postService.getPostAndNo();
    log.info("---> size {} data {}", postAndNo.size(), JSONObject.toJSONString(postAndNo));
  }

  @Test
  public void test4() {
    List<NameValue> postAndNo = postService.getPostHasRepAndNo();
    log.info("---> size {} data {}", postAndNo.size(), JSONObject.toJSONString(postAndNo));
  }

  @Test
  public void test5() {
    List<PostYears> postGroupBy = postService.getPostGroupBy("year");
    log.info("---> size {} data {}", postGroupBy.size(), JSONObject.toJSONString(postGroupBy));
  }

  @Test
  public void test6() {
    List<PostGroupByMonthVo> list = postService.getPostGroupByMonth();
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test7() {
    List<NameValue> list = userTbsService.getUserLevel();
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test8() {
    List<NameValue> list = postService.getPostTitlesyear("2014");
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));

  }

  @Test
  public void test9() {
    List<NameValue> list = commentService.getActiveUser(2012, 30);
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test10() {
    List<NameValue> list = userService.getUserFansBar(10);
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test11() {
    List<NameValue> list = userTbsService.getTbNameWordCloud(300);
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test12() {
    List<NameValue> list = userService.getUserGender();
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test13() {
    List<NameValue> list = userService.getUsertbAge(10);
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test14() {
    List<NameValue> list = postService.getPostUserTopBar(10);
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }

  @Test
  public void test15() {
    List<NameValue> list = wordDivideService.nameValues(WordDivideType.CONTENT, 20);
    log.info("---> size {} data {}", list.size(), JSONObject.toJSONString(list));
  }
}
