package com.megvii.spider.test.spider;

import java.util.Date;
import java.util.List;
import java.util.Random;
import com.megvii.dzh.spider.common.utils.CrowProxyProvider;
import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.UserAgentUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.utils.HttpConstant;

public class PostPageProcessor implements PageProcessor {

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
    //{"code":0,"data":{"pageNo":1,"pageSize":60,"records":[{"addr":{"code":"","msg":""},"albumCoreId":"f2273695-7b82-a0f1-ac74-f2c948fad578","birthday":"","cmnt":"","coreId":"a67-AAABZcLctEB_N4HKAAAABQ==","createTime":1536664480944,"faceId":"67-AAABZcLctEB_N4HKAAAABQ==","gender":"9","id":"5b963cb3c3bdcb0008db1821","identity":[{"code":"sfz","value":""}],"name":"成活","nation":{"msg":""},"src":"test","st":"","url":"http://10.199.2.27:8080/v4/objectStorage/face-photo-image-error/weed---11-_186d63f1bc31f7?content-type=image/jpeg"},"msg":"成功"}
    String msg = new JsonPathSelector("$.msg").select(rawText);
    List<String> urlList = new JsonPathSelector("$.data.records[*].url").selectList(page.getRawText());
    System.out.println("==============="+rawText);
  }

  private void crawlIpCount(Page page) {

  }

  public static void main(String[] args) {
    HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
    // 设置动态转发代理，使用定制的ProxyProvider
    httpClientDownloader.setProxyProvider(CrowProxyProvider.from(new Proxy("forward.xdaili.cn", 80)));
    //创建一个post请求
    String paramJson="{\"albumCoreId\":\"f2273695-7b82-a0f1-ac74-f2c948fad578\",\"nation\":{},\"identity\":{},\"addr\":{},\"pageNo\":1,\"pageSize\":60}";
    Request request = new Request("http://10.199.2.64/api/picture/v5/photo/list");
    request.setRequestBody(HttpRequestBody.json(paramJson, "UTF-8"));
    request.setMethod(HttpConstant.Method.POST);
    //
    Spider.create(new PostPageProcessor())//
      .addPipeline(new ConsolePipeline())//
      .addRequest(request)//
      .thread(4)//
      .run();
  }
}
