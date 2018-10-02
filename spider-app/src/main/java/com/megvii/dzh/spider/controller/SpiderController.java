package com.megvii.dzh.spider.controller;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IUserTbsService;
import com.megvii.dzh.spider.service.IWordDivideService;
import com.megvii.dzh.spider.webmagic.pipelines.PostDownloadPipeline;
import com.megvii.dzh.spider.webmagic.processors.PostProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

@RestController
@RequestMapping("/")
@Api(description = "爬虫接口控制器")
@Slf4j
public class SpiderController {

  @Autowired
  private BootConfig bootConfig;

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

  private Spider spider = Spider.create(new PostProcessor());

  @ApiOperation("启动爬虫")
  @ApiImplicitParams({})
  @RequestMapping(value = "/startSpider", method = {RequestMethod.GET})
  public String startSpider() {
    try {
      if (spider.getThreadAlive() > 0) {
        return "爬虫程序已启动,请勿重新请求";
      }

      long count = postService.count(new Post()) + commentService.count(new Comment()) + userService
          .count(new User()) +
          userTbsService.count(new UserTbs()) + wordDivideService.count(new WordDivide());

      if (count > 0) {
        return "库中有数据,请truncate table 后在执行爬虫";
      }

      // 启动多少个线程
      int spiderThreads = bootConfig.getSpiderThreads();
      // 贴吧名称
      String tbName = URLEncoder.encode(Constant.getTbName(), "UTF-8");
      // 开启爬虫
      spider.addUrl(
          bootConfig.getSpiderHttpType() + "://tieba.baidu.com/f?kw=" + tbName + "&ie=utf-8&pn=0")//
          .addPipeline(SpringUtils.getBean(PostDownloadPipeline.class))//
          .thread(spiderThreads)//
          .runAsync();
      //
      return "启动爬虫成功";
    } catch (Exception e) {
      log.error("startSpider error:{}", e);
    }
    return "启动失败";
  }

  @ApiOperation("爬虫状态")
  @ApiImplicitParams({})
  @RequestMapping(value = "/spiderStatus", method = {RequestMethod.GET})
  public String spiderStatus() {

    int threadAlive = spider.getThreadAlive();
    long pageCount = spider.getPageCount();
    //
    Map<String, Object> map = new HashMap<>();
    map.put("threadAlive", threadAlive);
    map.put("pageCount", pageCount);

    return JSONObject.toJSONString(map);
  }


}
