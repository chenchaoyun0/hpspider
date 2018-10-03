package com.megvii.dzh.spider.webmagic.processors;

import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.DateConvertUtils;
import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.SpiderFileUtils;
import com.megvii.dzh.spider.common.utils.UserAgentUtil;
import com.megvii.dzh.spider.domain.po.User;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
public class FollowingProcessor implements PageProcessor {


  /**
   * 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
   */
  private Site site = Site.me()//
      .addHeader("Proxy-Authorization", //
          ProxyGeneratedUtil.authHeader(Constant.ORDER_NUM, Constant.SECRET,
              (int) (System.currentTimeMillis() / 1000)))//
      .setDisableCookieManagement(true).setCharset("UTF-8")//
      .setTimeOut(60000)//
      .setRetryTimes(10)//
      .setSleepTime(new Random().nextInt(20) * 100)//
      .setUserAgent(UserAgentUtil.Mac_AGENT);

  @Override
  public Site getSite() {
    return site;
  }

  @Override
  public void process(Page page) {
    try {
      String fanscStr = page.getHtml().xpath("//*[@id=\"content\"]/div[2]/div[1]/ul/li[1]/a/span/text()").toString();
      String folcStr = page.getHtml().xpath("//*[@id=\"content\"]/div[2]/div[1]/ul/li[2]/a/span/text()").toString();
      log.info("fanscStr {},folcStr {}",fanscStr,folcStr);
      User userFollow = new User();
      userFollow.setFansCount(Integer.parseInt(fanscStr.replaceAll("[^0-9]", "")));
      userFollow.setFollowCount(Integer.parseInt(folcStr.replaceAll("[^0-9]", "")));

      page.putField("userFollow",userFollow);

    } catch (Exception e) {
      log.error("ProfileProcessor error {}", e);
      SpiderFileUtils.writeString2local(page.getHtml().toString(), "E://hp-spider//userFollow-error.html");
    }
  }
}
