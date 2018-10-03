package com.megvii.dzh.spider.webmagic.processors;

import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.constant.Constant;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
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
   * 匹配贴吧首页过滤
   */
  private static final String TB_HOME = "https://bbs\\.hupu\\.com/bxj-\\d+";
  /**
   * 匹配贴吧首页帖子分页
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
  public AtomicLong totalPost = new AtomicLong();
  public AtomicLong totalComment = new AtomicLong();
  public AtomicLong totalUser = new AtomicLong();

  private ProfileProcessor profileProcessor= new ProfileProcessor();
  private FollowingProcessor followingProcessor= new FollowingProcessor();

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
   * 更换字段agent 有可能变成手机客户端，影响爬虫
   */

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
      log.info("---> 当前爬取论坛第【{}】页，已爬取帖子【{}】条，帖子回复【{}】，用户主页【{}】", (pageNo), totalPost.get(), totalComment.get(), totalUser.get());
      int sizePostQueue = bootConfig.getThreadPoolPost().arrayBlockingQueue.size();
      int sizeCommentQueue = bootConfig.getThreadCommentDivide().arrayBlockingQueue.size();
      int sizeUserQueue = bootConfig.getThreadUserDivide().arrayBlockingQueue.size();
      log.info("---> 当队列堆积 post【{}】，comment【{}】，user【{}】",sizePostQueue,sizeCommentQueue,sizeUserQueue);

      /**
       * 若是贴吧首页则将所有帖子加入待爬取队列
       */
      if (url.matches(TB_HOME)) {
        //将所有帖子页面加入队列
        //SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//postlist.html");
        log.info("--> 当前爬取主页URL {}",url);
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
        List<String> all = page.getHtml().links().regex(USER_HOME).all();
        page.addTargetRequests(all);// 用户主页
        commentData(page, html);
      }

      /**
       * 用户主页url
       */
      if (page.getUrl().regex(USER_HOME).match()) {
       SpiderFileUtils.writeString2local(html.toString(), "E://hp-spider//userHome.html");
        List<String> allUserHome = html.links().regex(USER_HOME).all();
        page.addTargetRequests(allUserHome);
        crawlUser(page, html);
      }

      /**
       * 贴吧分页，要爬的贴吧分好页，加入待爬取队列
       */
      if (url.matches(TB_HOME)) {
        /**
         * 判断当前爬取页是否超过限制
         */
        if (pageNo < bootConfig.getSpiderPostSize()) {
          log.info("---------> 继续爬取第【{}】页 贴吧 <-----------", pageNo);
          page.addTargetRequest(TB_HOME_PAGE + pageNo);
          pageNo = pageNo + 1;
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
      if(StringUtils.isBlank(title)){
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
      post.setLightTotal(Long.parseLong(StringUtils.isBlank(trim)?"0":trim));
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
        String light = html.xpath("//*[@id=\""+idLight+"\"]/div/div[2]/div[1]/div[1]/span[4]/span/span/text()").toString();
        if(StringUtils.isBlank(light)){
          light = html.xpath("//*[@id=\""+idLight+"\"]/div/div[2]/div[1]/div[1]/span[6]/span/span/text()").toString();
        }
        String content = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/p/text()").toString();
        String device = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/small/a/text()").toString();
        if (StringUtils.isBlank(content)) {
          content = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/text()").toString();
        }
        if (StringUtils.isBlank(device)) {
          device = html.xpath("//*[@id=\"" + idLight + "\"]/div/div[2]/table/tbody/tr/td/small/text()").toString();
        }
        //回复的数据
        Comment comment = new Comment();
        comment.setUserName(userName);
        comment.setPostUrl(url);
        comment.setTime(DateConvertUtils.parse(time, DateConvertUtils.DATE_TIME_NO_SS));
        comment.setUserDevice(StringUtils.substringAfter(device,"虎扑"));
        comment.setLightCount(Long.parseLong(StringUtils.isBlank(light) ? "0" : light));
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
      String userName = html.xpath("//div[@itemprop='name']/text()").toString();
      String userHeadUrl = html.xpath("//*[@id=\"j_head\"]/@src").get();
      if(StringUtils.isBlank(userName)||StringUtils.isBlank(userHeadUrl)){
        return;
      }
      String address = html.xpath("//div[@class='personalinfo']//span[@itemprop='address']/text()").toString();
      String provice = StringUtils.substringBefore(address, "省");
      String city = StringUtils.substringAfter(address, "省");
      long viewTotal = Long.parseLong(page.getHtml().xpath("//div[@class='personal']//span[@class='f666'][1]/text()")
          .toString().replaceAll("[^0-9]", ""));
      //

      ResultItems resultProfile = Spider.create(profileProcessor).get(url + "/profile");
      User userProfile = (User) resultProfile.getAll().get("userProfile");
      //
      ResultItems resultFollow = Spider.create(followingProcessor).get(url + "/following");
      User userFollow = (User) resultFollow.getAll().get("userFollow");
      //
      User user = new User();
      user.setUserName(userName);
      user.setProvince(provice);
      user.setCity(city);
      user.setGender(userProfile.getGender());
      user.setLevel(userProfile.getLevel());
      user.setJoinDate(userProfile.getJoinDate());
      user.setFollowCount(userFollow.getFollowCount());
      user.setFansCount(userFollow.getFansCount());
      user.setUserHeadUrl(userHeadUrl);
      user.setUserHomeUrl(url);
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
        .addUrl("https://my.hupu.com/91599511791114")//
        .addPipeline(new ConsolePipeline())//
        .thread(1)//
        .run();
  }
}
