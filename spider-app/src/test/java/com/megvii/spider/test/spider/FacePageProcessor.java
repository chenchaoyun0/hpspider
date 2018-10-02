package com.megvii.spider.test.spider;

import java.util.Date;
import java.util.Random;
import com.megvii.dzh.spider.common.utils.CrowProxyProvider;
import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.UserAgentUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Json;

public class FacePageProcessor implements PageProcessor {

  private static final String ORDER_NUM = "ZF201710169692T66jkr";// 讯代理订单号
  private static final String SECRET = "3b23ace31a2447baa44d624e9c5fd0f5";// 讯代理密码

  // 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
  private Site site = Site.me()//
    .addHeader("Proxy-Authorization",
      ProxyGeneratedUtil.authHeader(ORDER_NUM, SECRET, (int)(new Date().getTime() / 1000)))// 设置代理
    .setDisableCookieManagement(true).setCharset("UTF-8")//
    .setTimeOut(30000)//
    .setRetryTimes(3)//
    .setSleepTime(new Random().nextInt(20) * 100)//
    .addHeader("Authorization", "yWBvmZWtfIipYAqGiiEMvvlPjyDHRYUiCWxECkSsoiyfOrlWcpbcXNnujXVa")//
    .setUserAgent(UserAgentUtil.getRandomUserAgent());

  @Override
  public Site getSite() {
    return site;
  }

  @Override
  public void process(Page page) {
    Json json = page.getJson();
    // 获取所有页
    String rawText = page.getRawText();
    System.out.println("==============="+rawText);
  }

  private void crawlIpCount(Page page) {

  }

  public static void main(String[] args) {
    HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
    // 设置动态转发代理，使用定制的ProxyProvider
    httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
    //
    Spider.create(new FacePageProcessor())//
      .addPipeline(new ConsolePipeline())//
      .addUrl("http://10.199.2.64/#/resource/database/pictures/5b9627940e9fe1000cb1ce10/all")//
      .thread(4)//
      .run();
  }
}
