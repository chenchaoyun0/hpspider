package com.megvii.dzh.spider.common.config;

import com.megvii.dzh.perfrom.concurrent.pool.ThreadPool;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class BootConfig {

  @Value("${server.port}")
  private String serverPort;
  /**
   * 爬取的线程数
   */
  @Value("${spider.threads:100}")
  private int spiderThreads;

  /**
   * 重试次数
   */
  @Value("${site.retry.times:4}")
  private String siteRetryTimes;
  
  /**
   * 抓取间隔
   */
  @Value("${site.sleep.time}")
  private String siteSleepTime;
  /**
   * 抓取间隔
   */
  @Value("${spider.post.size:100000}")
  private int spiderPostSize;
  /**
   *
   */
  @Autowired
  private ThreadPool<Post> threadPoolPost;
  /**
   *
   */
  @Autowired
  private ThreadPool<Object> threadCommentDivide;
  /**
   *
   */
  @Autowired
  private ThreadPool<User> threadUserDivide;
}
