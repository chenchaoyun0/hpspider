package com.megvii.dzh.spider.webmagic.processors;

import com.megvii.dzh.spider.SpiderApplication;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.DateConvertUtils;
import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.SpiderFileUtils;
import com.megvii.dzh.spider.common.utils.UserAgentUtil;
import com.megvii.dzh.spider.domain.po.User;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Slf4j
public class ProfileProcessor implements PageProcessor {


  /**
   * 抓取网站的相关配置，包括编码、抓取间隔、重试次数、代理、UserAgent等
   */
  private Site site = Site.me()//
      .addHeader("Proxy-Authorization", //
          ProxyGeneratedUtil.authHeader(Constant.ORDER_NUM, Constant.SECRET,
              (int) (System.currentTimeMillis() / 1000)))//
      //.setDisableCookieManagement(true).setCharset("UTF-8")//
      .setTimeOut(60000)//
      .setRetryTimes(10)//
      .setSleepTime(new Random().nextInt(20) * 100)//
      .setUserAgent(UserAgentUtil.Mac_AGENT)//
      .addCookie("PHPSESSID", "00gs11ofp2l9o0rv4dvs4rmjp0")//
      .addCookie("_HUPUSSOID", "79521e1e-fec2-4647-b2c8-e4dc84dd8ac4")//
      .addCookie("_cnzz_CV30020080", "buzi_cookie%7Cf2f994b1.5bad.47cb.9630.a884c6abed3f%7C-1")//
      .addCookie("_dacevid3", "f2f994b1.5bad.47cb.9630.a884c6abed3f")//
      .addCookie("__gads", "ID=3d444c38736e39ba:T=1525453594:S=ALNI_MadENIKu0QwTvgGLLdj2P8yQiY6bQ")//
      .addCookie("_CLT", "918ebe7bb324d8673460f7af1d701a5c")//
      .addCookie("_cnzz_CV30020080", "buzi_cookie%7Cf2f994b1.5bad.47cb.9630.a884c6abed3f%7C-1")//
      .addCookie("_fmdata", "DSdOLR7a04vm%2FZZ%2BwLNRzWgiOuRQ0E0h92RuzV6mVc6AmQCHrcdFoA0tAro7HLZpLaeFakuKhLkYA%2Bge7DQXMjapGTEzPOq5tQ63T3WATfo%3D")//
      .addCookie("AUM", "dgR7lnYDBW69CyJJpHu_3es3jN5nt-yqlWtWpTno5j7lw")//
      .addCookie("u", "26414109|6Im+54m557Gz5pav54m5|22f3|486fd412f17389a79e54ca6d893d80f5|f17389a79e54ca6d|6Im+54m557Gz5pav54m5")//
      .addCookie("us", "a3cacb5a02eb4ca38a0be1dfeb9833c90457818bcc6ac7d941f6a93f2b13a8a7119641e1a1b7744bbf9f1630d902539b9596260d66b3cf0c156e9255d1c708f9")//
      .addCookie("ua", "153855440")//
      .addCookie("__dacevst", "04f4a1fa.74c0e730|" + System.currentTimeMillis());

  @Override
  public Site getSite() {
    return site;
  }

  @Override
  public void process(Page page) {
    try {
      Html pageHtml = page.getHtml();
      String sss = pageHtml.xpath("//*[@id=\"search_main\"]/div/h4/text()").toString();
      List<String> all = pageHtml.xpath("//*[@id=\"content\"]/table[1]/tbody/tr").all();
      String joinDateStr=null;
      String levelStr=null;
      String sex=null;
      for (int i = 1; i <= all.size(); i++) {
        ////*[@id="content"]/table[1]/tbody/tr[1]/td[1]
        String key = pageHtml.xpath("//*[@id=\"content\"]/table[1]/tbody/tr[" + i + "]/td[1]/text()").toString();
        if ("性别：".equals(key)) {
          sex = pageHtml.xpath("//*[@id=\"content\"]/table[1]/tbody/tr[" + i + "]/td[2]/text()").toString();
        }
        if ("注册时间：".equals(key)) {
          joinDateStr = pageHtml.xpath("//*[@id=\"content\"]/table[1]/tbody/tr[" + i + "]/td[2]/text()").toString();
        }
        if ("论坛等级：".equals(key)) {
          levelStr = pageHtml.xpath("//*[@id=\"content\"]/table[1]/tbody/tr[" + i + "]/td[2]/text()").toString();
        }
      }


      //log.info("joinDateStr {},levelStr {}", joinDateStr, levelStr);
      User userProfile = new User();
      userProfile.setJoinDate(DateConvertUtils.parse(joinDateStr, DateConvertUtils.DATE_FORMAT));
      userProfile.setLevel(Integer.parseInt(levelStr));
      userProfile.setGender("男".equals(sex)?1:("女".equals(sex)?0:-1));
      page.putField("userProfile", userProfile);

    } catch (Exception e) {
      log.error("ProfileProcessor error url {}", page.getRequest().getUrl(), e);
      SpiderFileUtils.writeString2local(page.getHtml().toString(), "E://hp-spider//userProfile-error.html");

    }

  }

  public static void main(String args[]) {
    Spider.create(new ProfileProcessor())//
        .addUrl("https://my.hupu.com/150996019445837/profile")//
        .addPipeline(new ConsolePipeline())//
        .thread(1)//
        .run();
  }
}
