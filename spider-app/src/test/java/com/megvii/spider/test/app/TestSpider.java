package com.megvii.spider.test.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.megvii.dzh.spider.SpiderApplication;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = {SpiderApplication.class})
@Slf4j
public class TestSpider {

  @Test
  public void test() {
  }
}
