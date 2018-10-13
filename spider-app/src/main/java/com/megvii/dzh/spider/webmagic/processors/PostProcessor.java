package com.megvii.dzh.spider.webmagic.processors;

import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.CookieUtils;
import com.megvii.dzh.spider.common.utils.DateConvertUtils;
import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.SpiderFileUtils;
import com.megvii.dzh.spider.common.utils.SpiderStringUtils;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.common.utils.URLGeneratedUtil;
import com.megvii.dzh.spider.common.utils.UserAgentUtil;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

@Slf4j
public class PostProcessor implements PageProcessor {

  /**
   * 匹配帖子地址
   */
  private static final String POST_URL = "/\\d+\\.html";
  /**
   * 匹配虎扑步行街首页过滤
   */
  private static final String TB_HOME = "https://bbs\\.hupu\\.com/bxj-\\d+";
  /**
   * 匹配虎扑步行街首页帖子分页
   */
  private static final String TB_HOME_PAGE = "https://bbs.hupu.com/bxj-";
  /**
   * 帖子详情页,第一页
   */
  private static final String POST_DETAIL = "https://bbs\\.hupu\\.com/\\d+\\.html";
  /**
   * 帖子详情页,第一页往后
   */
  private static final String POST_DETAIL_AFTER = "https://bbs\\.hupu\\.com/\\d+\\-\\d+.html";

  /**
   * 匹配用户主页
   */
  private static final String USER_HOME = "https://my\\.hupu\\.com/\\d+";

  /**
   * 爬取起始页，每页为50条帖子
   */
  private long pageNo = 2;

  private BootConfig bootConfig = SpringUtils.getBean(BootConfig.class);

  /**
   * 计数
   */
  public volatile AtomicLong totalPost = new AtomicLong();
  public volatile AtomicLong totalComment = new AtomicLong();
  public volatile AtomicLong totalUser = new AtomicLong();

  private volatile Set<String> userHomeList = new HashSet<>();

  /**
   * 方便本地测试
   */
  public PostProcessor() {
    if (bootConfig == null) {
      bootConfig = new BootConfig();
      bootConfig.setSpiderPostSize(10);
    }
  }

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
      .setUserAgent(UserAgentUtil.Mac_AGENT);

  @Override
  public Site getSite() {
    String cookieStr = "_dacevid3=f2f994b1.5bad.47cb.9630.a884c6abed3f; __gads=ID=3d444c38736e39ba:T=1525453594:S=ALNI_MadENIKu0QwTvgGLLdj2P8yQiY6bQ; _HUPUSSOID=79521e1e-fec2-4647-b2c8-e4dc84dd8ac4; _CLT=918ebe7bb324d8673460f7af1d701a5c; AUM=dgR7lnYDBW69CyJJpHu_3es3jN5nt-yqlWtWpTno5j7lw; lastvisit=276%091538559002%09%2Ferror%2F%40_%40.php%3F; u=26414109|6Im+54m557Gz5pav54m5|22f3|486fd412f17389a79e54ca6d893d80f5|f17389a79e54ca6d|6Im+54m557Gz5pav54m5; us=47d47f6d4078d22f86432e59e24915d2694220a778a038e9a0c2e578d423c549db33db4cad17cf0113229b9a5c9d95c55e94f664be867556d0aef5af2cc32cac; _cnzz_CV30020080=buzi_cookie%7Cf2f994b1.5bad.47cb.9630.a884c6abed3f%7C-1; Hm_lvt_39fc58a7ab8a311f2f6ca4dc1222a96e=1538568189,1538568350,1538568979,1538631445; PHPSESSID=2fbd8f3df5d23b9daa83ba2928519431; _fmdata=4y926Zzt8Ck%2BeVTmL2EVELFeapXRbe75dF7sIOPhFNT8plhPmUexOiapwqzpK0%2BclXnJrJNvTPj0O5cmEfdyInDQZhjI4wiYtf0fVY3CSQE%3D; ua=153863973;Hm_lpvt_39fc58a7ab8a311f2f6ca4dc1222a96e=1538644271";
    Map<String, String> cookiesMap = CookieUtils.getCookiesMap(cookieStr);
    for (Entry<String, String> entry : cookiesMap.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      site.addCookie(key, value);
    }
    site.addCookie("__dacevst", "53ac8b5d.fe349230|" + System.currentTimeMillis());
    return site;
  }

  private long logLoop = 0;

  /**
   * 爬取处理
   */
  @Override
  public void process(Page page) {
    long start = System.currentTimeMillis();
    String url = page.getRequest().getUrl();
    log.debug("---> url {}", url);
    Html html = page.getHtml();
    try {
      if (logLoop++ % 30 == 0&&logLoop>1) {
        log.info("---> 当前线程【{}】，爬取URL【{}】", Thread.currentThread().getName(), url);
        log.info("---> 当前爬取论坛第【{}】页，已爬取帖子【{}】条，帖子回复【{}】，用户主页【{}】", (pageNo), totalPost.get(), totalComment.get(), totalUser.get());
        int sizePostQueue = bootConfig.getThreadPoolPost().arrayBlockingQueue.size();
        int sizeCommentQueue = bootConfig.getThreadCommentDivide().arrayBlockingQueue.size();
        int sizeUserQueue = bootConfig.getThreadUserDivide().arrayBlockingQueue.size();
        int sizeWordDivideQueue = bootConfig.getWordDivideSaveExcute().arrayBlockingQueue.size();
        log.info("---> 当队列堆积 post【{}】，comment【{}】，user【{}】，wordDivide【{}】，userHomeList 【{}】", sizePostQueue, sizeCommentQueue, sizeUserQueue,sizeWordDivideQueue, userHomeList.size());
      }

      /**
       * 若是虎扑步行街首页则将所有帖子加入待爬取队列
       */
      if (url.matches(TB_HOME)) {
        //将所有帖子页面加入队列
        //SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//postlist.html");
        //log.info("--> 当前爬取主页URL {}", url);
        List<String> listPosts = html.links().regex(POST_URL).all();
        listPosts.forEach(e -> URLGeneratedUtil.generatePostURL(e));
        page.addTargetRequests(listPosts);
        //用户主页
        List<String> allUserHome = html.links().regex(USER_HOME).all();
        page.addTargetRequests(allUserHome);
      }

      /**
       * 匹配帖子详情页url
       */
      if (page.getUrl().regex(POST_DETAIL).match()) {
        //log.info("--> 当前爬取匹配帖子详情页url {}", url);
        //SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//postDetail.html");
        List<String> all = page.getHtml().links().regex(USER_HOME).all();
        page.addTargetRequests(all);// 用户主页
        crawlPost(page, html);
      }
      /**
       * 匹配帖子详情页url 分页 即回复页
       */
      if (page.getUrl().regex(POST_DETAIL_AFTER).match()) {
        //SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//postDetailAfter.html");
        //log.info("--> 当前爬取匹配帖子详情页 分页 即回复页URL {}", url);
        List<String> all = page.getHtml().links().regex(USER_HOME).all();
        page.addTargetRequests(all);// 用户主页
        commentData(page, html);
      }

      /**
       * 用户主页url
       */
      if (page.getUrl().regex(USER_HOME).match()) {
        //log.info("--> 当前爬取用户主页url {}", url);
        //SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//userHome.html");
        List<String> allUserHome = html.links().regex(USER_HOME).all();
        userHomeList.addAll(allUserHome);
        //page.addTargetRequests(allUserHome);
        crawlUser(page, html);
      }

      /**
       * 虎扑步行街分页，要爬的虎扑步行街分好页，加入待爬取队列
       */
      if (url.matches(TB_HOME)) {
        /**
         * 判断当前爬取页是否超过限制
         */
        if (pageNo < bootConfig.getSpiderPostSize()) {
          log.info("---------> 继续爬取第【{}】页 论坛 <-----------", pageNo);
          page.addTargetRequest(TB_HOME_PAGE + pageNo);
          pageNo = pageNo + 1;
        } else {
          page.addTargetRequests(new ArrayList<>(userHomeList));
        }
      }


    } catch (Exception e) {
      log.error("PostDetailProcessor error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      //SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//" + uuid + ".html");
    } finally {
    }

  }


  /**
   * 帖子页 获取帖子数据
   */
  private void crawlPost(Page page, Html html) {
    String url = page.getRequest().getUrl();
    try {
      /**
       * 有多少页回复
       */
      String dataMaxpage = html.xpath("//*[@id=\"t_main\"]/div[2]/div[2]/h1/@data-maxpage").get();
      int commentPageSize = Integer.parseInt(StringUtils.isBlank(dataMaxpage) ? "0" : dataMaxpage);
      if (commentPageSize > 1) {
        for (int i = 2; i <= commentPageSize; i++) {
          String before = StringUtils.substringBefore(url, ".html");
          String nextPage = before + "-" + i + ".html";
          page.addTargetRequest(nextPage);
        }
      }

      /**
       * 主题信息
       */
      Post post = new Post();
      String title = html.xpath("//div[@class='bbs-hd-h1']/h1[@id='j_data']/text()").toString();
      if (StringUtils.isBlank(title)) {
        return;
      }
      String time = html.xpath("//*[@id=\"tpc\"]/div/div[2]/div[1]/div[1]/span[@class='stime']/text()").toString();
      //
      List<String> allp = html.xpath("//*[@id=\"tpc\"]/div/div[2]/table[1]/tbody/tr/td/div[2]/p").all();
      String content = html.xpath("//*[@id=\"tpc\"]/div/div[2]/table[1]/tbody/tr/td/div[2]/text()").toString();
      if (StringUtils.isBlank(content) && !CollectionUtils.isEmpty(allp)) {
        for (int i = 1; i <= allp.size(); i++) {
          content = content + html.xpath("//*[@id=\"tpc\"]/div/div[2]/table[1]/tbody/tr/td/div[2]/p[" + i + "]/text()").toString();
        }
      }

      int replyNum = Integer.parseInt(page.getHtml().xpath("//div[@class='bbs-hd-h1']/span[1]/span[1]/text()").toString().replaceAll("[\\u4e00-\\u9fa5]+", ""));
      String userName = page.getHtml().xpath("//div[@class='author']//a[@class='u'][1]/text()").toString();
      String lightStr = html.xpath("//*[@id=\"t_main\"]/div[2]/div[2]/span/span[2]/text()").toString();

      // 保存数据
      post.setContent(SpiderStringUtils.xffReplace(content.trim()));
      post.setPostUrl(url);
      String trim = lightStr.replaceAll("[^0-9]", "").trim();
      post.setLightTotal(Long.parseLong(StringUtils.isBlank(trim) ? "0" : trim));
      post.setReplyNum(replyNum);
      post.setTime(DateConvertUtils.parse(time, DateConvertUtils.DATE_TIME_NO_SS));
      post.setTitle(SpiderStringUtils.xffReplace(title));
      post.setUserName(SpiderStringUtils.xffReplace(userName));
      //
      page.putField("post", post);
      totalPost.incrementAndGet();
      /**
       * 回复信息
       */
      if (replyNum > 0) {
        commentData(page, html);
      }

    } catch (Exception e) {
      log.error("crawlPost error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//post-" + uuid + ".html");
    }
  }


  private void commentData(Page page, Html html) {
    String url = page.getRequest().getUrl();
    try {
      List<Comment> listComment = new ArrayList<>();
      /**
       * 亮评数量
       */

      List<String> lightSize = html.xpath("//*[@id=\"t_main\"]/form/[@class=\"floor\"]/@id").all();
      lightSize.remove("tpc");
      for (int i = 0; i < lightSize.size(); i++) {
        String idLight = lightSize.get(i);
        String userName = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/div[1]/div[1]/a/text()").toString();
        String time = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/div[1]/div[1]/span[2]/text()").toString();
        String light = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/div[1]/div[1]/span[4]/span/span/text()").toString();
        if (StringUtils.isBlank(light)) {
          light = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/div[1]/div[1]/span[6]/span/span/text()").toString();
        }
        String content = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/p/text()").toString();
        String device = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/small/a/text()").toString();
        if (StringUtils.isBlank(content)) {
          content = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/text()").toString();
        }
        if (StringUtils.isBlank(device)) {
          device = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/small/text()").toString();
        }
        //
        long lightLong = Long.parseLong(StringUtils.isBlank(light) ? "0" : light);
        if(lightLong<1){
          continue;
        }
        //回复的数据
        Comment comment = new Comment();
        comment.setUserName(userName);
        comment.setPostUrl(url);
        comment.setTime(DateConvertUtils.parse(time, DateConvertUtils.DATE_TIME_NO_SS));
        comment.setUserDevice(StringUtils.substringAfter(device, "虎扑"));
        comment.setLightCount(lightLong);
        comment.setContent(SpiderStringUtils.xffReplace(content));
        listComment.add(comment);

      }

      page.putField("listComment", listComment);
      totalComment.addAndGet(listComment.size());

    } catch (Exception e) {
      log.error("commentData error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//comment" + uuid + ".html");
    }
  }

  /**
   * 帖子页 获取帖子数据
   */
  private void crawlUser(Page page, Html html) {
    String url = page.getRequest().getUrl();
    try {

      String sss = html.xpath("//*[@id=\"search_main\"]/div/h4/text()").toString();

      String userName = html.xpath("//div[@itemprop='name']/text()").toString();
      String userHeadUrl = html.xpath("//*[@id=\"j_head\"]/@src").get();
      String lighStr = html.xpath("//div[@class='personal']//span[@class='f666'][1]/text()").toString();
      if (StringUtils.isBlank(userName) || StringUtils.isBlank(lighStr)) {
        return;
      }
      long viewTotal = Long.parseLong(lighStr.replaceAll("[^0-9]", ""));
      List<String> personalinfo = html.xpath("//*[@id=\"main\"]//div[@class='personalinfo']/span").all();
      if (CollectionUtils.isEmpty(personalinfo)) {
        return;
      }

      String address = null;
      String provice = null;
      String city = null;
      String joinDateStr = null;
      String levelStr = null;
      String sex = null;
      String sheqSw = null;
      String onlineHours = null;
      String affiliation = null;
      boolean flagRenz = false;
      for (int i = 1; i <= personalinfo.size(); i++) {
        String key = html.xpath("//*[@id=\"main\"]//div[@class='personalinfo']/span[" + i + "]/text()").toString();
        if ("性        别：".equals(key)) {
          sex = html.xpath("//*[@id=\"main\"]//div[@class='personalinfo']//span[@itemprop='gender']/text()").toString();
          continue;
        }
        if ("所  在  地：".equals(key)) {
          address = html.xpath("//div[@class='personalinfo']//span[@itemprop='address']/text()").toString();
          provice = StringUtils.substringBefore(address, "省");
          city = StringUtils.substringAfter(address, "省");
          continue;
        }
        if ("NBA主队：".equals(key)) {
          affiliation = html.xpath("//div[@class='personalinfo']//span[@itemprop='affiliation']/a/text()").toString();
          continue;
        }
        if ("认证信息：".equals(key)) {
          flagRenz = true;
        }
      }

      String arrayValues = html.xpath("//div[@class='personalinfo']/text()").toString();
      String[] split = StringUtils.split(arrayValues, " ");
      List<String> stringList = html.xpath("//*[@id=\"main\"]/div[1]/div[2]/div/span[@class='f666']/text()").all();
      if(split.length>5){
          split = ArrayUtils.subarray(split, split.length-5, split.length);
      }
      if(!stringList.contains("上次登录：")){
        split = ArrayUtils.subarray(split, split.length-4, split.length);
      }
      Map<String,String> perMap=new HashMap<>();
      List<String> stringListNew=new ArrayList<>();
      String[] arrays={"社区声望：","社区等级：","在线时间：","加入时间："};
      for (int i=0;i<stringList.size();i++) {
        String key = stringList.get(i);
        if(ArrayUtils.contains(arrays,key)){
          stringListNew.add(key);
        }
      }
      for (int i=0;i<stringListNew.size();i++) {
        String key = stringListNew.get(i);
        if(ArrayUtils.contains(arrays,key)){
          perMap.put(key,split[i]);
        }
      }

      sheqSw = perMap.get("社区声望：");
      levelStr = perMap.get("社区等级：");
      onlineHours = perMap.get("在线时间：");
      joinDateStr =perMap.get("加入时间：");

      String folStr = html.xpath("//*[@id=\"following\"]//p[@class='more']/a[1]/text()").toString();
      String fanStr = html.xpath("//*[@id=\"following\"]//p[@class='more']/a[2]/text()").toString();

      //
      int gender = StringUtils.isBlank(sex) ? -1 : "男".equals(sex) ? 1 : 0;
      int level = Integer.parseInt(StringUtils.isBlank(levelStr) ? "0" : levelStr);
      Date joinDate = DateConvertUtils.parse(joinDateStr, DateConvertUtils.DATE_FORMAT);
      int flc = Integer.parseInt(StringUtils.isBlank(folStr) ? "0" : folStr.replaceAll("[^0-9]", ""));
      int fac = Integer.parseInt(StringUtils.isBlank(fanStr) ? "0" : fanStr.replaceAll("[^0-9]", ""));
      long online = Long.parseLong(StringUtils.isBlank(onlineHours) ? "0" : onlineHours.replaceAll("[^0-9]", ""));
      long sheqswlong = Long.parseLong(StringUtils.isBlank(sheqSw) ? "0" : sheqSw.replaceAll("[^0-9]", ""));
      //
      //封装数据
      User user = new User();
      user.setUserName(userName);
      user.setProvince(StringUtils.isBlank(provice) ? "未知" : provice);
      user.setCity(StringUtils.isBlank(city) ? "未知" : city);
      user.setOnlineHours(online);
      user.setGender(gender);
      user.setAffiliation(StringUtils.isBlank(affiliation) ? "未知" : affiliation);
      user.setLevel(level);
      user.setJoinDate(joinDate);
      user.setFollowCount(flc);
      user.setFansCount(fac);
      user.setUserHeadUrl(userHeadUrl);
      user.setUserHomeUrl(url);
      user.setSheqSw(sheqswlong);
      user.setViewTotal(viewTotal);
      //
      page.putField("user", user);
      totalUser.incrementAndGet();
    } catch (Exception e) {
      log.error("crawlUser error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//userHome" + uuid + ".html");
    }
  }

  public static void main(String[] args) {
    Spider.create(new PostProcessor())//
        //.addUrl("https://bbs.hupu.com/bxj-1")//
        //.addUrl("https://bbs.hupu.com/23799376.html")//
        .addUrl("https://bbs.hupu.com/23761335.html")//
        .addPipeline(new ConsolePipeline())//
        .thread(1)//
        .run();
  }
}
