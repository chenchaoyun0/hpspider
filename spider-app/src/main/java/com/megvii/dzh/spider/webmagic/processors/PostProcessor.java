package com.megvii.dzh.spider.webmagic.processors;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.DateConvertUtils;
import com.megvii.dzh.spider.common.utils.ProxyGeneratedUtil;
import com.megvii.dzh.spider.common.utils.SpiderFileUtils;
import com.megvii.dzh.spider.common.utils.SpiderStringUtils;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.common.utils.URLGeneratedUtil;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.domain.vo.PostUser;
import java.net.URLEncoder;
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
  private static final String POST_URL = "/p/\\d++";
  /**
   * 匹配贴吧首页过滤
   */
  private static final String TB_HOME = "://tieba.baidu.com/f\\?kw=(.*?)";
  /**
   * 匹配贴吧首页帖子分页
   */
  private static final String TB_HOME_PAGE = "://tieba.baidu.com/f?kw={0}&ie=utf-8&pn=";

  /**
   * 匹配用户主页
   */
  private static final String USER_HOME = "/home/main(.*?)";

  /**
   * 爬取起始页，每页为50条帖子
   */
  private long pageNo = 50;

  private BootConfig bootConfig = SpringUtils.getBean(BootConfig.class);

  /**
   * 计数
   */
  public AtomicLong totalPost = new AtomicLong();
  public AtomicLong totalComment = new AtomicLong();
  public AtomicLong totalUser = new AtomicLong();

  /**
   * 方便本地测试
   */
  public PostProcessor() {
    if (bootConfig == null) {
      bootConfig = new BootConfig();
      bootConfig.setSpiderPostSize(10);
      Constant.setSpiderHttpType("http");
      Constant.setTbName("太原理工大学");
    }
  }

  /**
   * 更换字段agent 有可能变成手机客户端，影响爬虫
   */
  private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:24.0) Gecko/20100101";

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
      .setUserAgent(userAgent);

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
      log.info("---> 当前爬取贴吧第【{}】页，已爬取帖子【{}】条，帖子回复【{}】，用户主页【{}】",( pageNo / 50),totalPost.get(),totalComment.get(),totalUser.get());

      /**
       * 若是贴吧首页则将所有帖子加入待爬取队列
       */
      if (url.matches(Constant.getSpiderHttpType() + TB_HOME)) {
        /**
         * 将所有帖子页面加入队列
         */
        //SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//postlist.html");
        List<String> listPosts = html.links().regex(POST_URL).all();
        //log.info("---> 当前页总帖数 {}", listPosts.size());
        listPosts.forEach(e -> URLGeneratedUtil.generatePostURL(e));
        page.addTargetRequests(listPosts);
      }

      /**
       * 匹配帖子页url
       */
      if (page.getUrl().regex(POST_URL).match()) {
//        SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//postDetail.html");
        String title = html.xpath("/html/head/title/text()").get();
        //有时候获取不到帖子标题
        if (title == null) {
          title = html.xpath("//*[@id=\"j_core_title_wrap\"]/h3/a/text()").toString();
        }
        if (title == null) {
          log.error("title is null...");
          //SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//title-null-post-" + UUID.randomUUID().toString() + ".html");
        }
        // 消失的帖子过滤
        if (StringUtils.isNotBlank(title) && title.indexOf("404") > 0) {
          return;
        }
        crawlPost(page, html);
      }

      /**
       * 用户主页url
       */
      if (page.getUrl().regex(USER_HOME).match()) {
//        SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//userHome.html");
        String title = html.xpath("/html/head/title/text()").get();
        if (StringUtils.isNotBlank(title) && title.indexOf("404") > 0) {
          return;
        }
        crawlUser(page, html);

      }
      /**
       * 贴吧分页，要爬的贴吧分好页，加入待爬取队列
       */
      if (url.matches(Constant.getSpiderHttpType() + TB_HOME)) {
        /**
         * 判断当前爬取页是否超过限制
         */
        if (pageNo < bootConfig.getSpiderPostSize()) {
          log.info("---------> 继续爬取第【{}】页 贴吧 <-----------", pageNo / 50);
          // 将贴吧名编码
          String tieBaName = URLEncoder.encode(Constant.getTbName(), "UTF-8");
          String match = MessageFormat
              .format(Constant.getSpiderHttpType() + TB_HOME_PAGE, tieBaName);
          page.addTargetRequest(match + pageNo);
          pageNo = pageNo + 50;
        }
      }


    } catch (Exception e) {
      log.error("PostDetailProcessor error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      //SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//" + uuid + ".html");
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
       * 过滤不是本校贴吧
       */
      String tbName = html.xpath("//*[@id=\"container\"]/div/div[1]/div[2]/div[2]/a/text()")
          .toString();
      if (StringUtils.isNotBlank(tbName) && tbName.indexOf(Constant.getTbName()) < 0) {
        return;
      }

      // 查看该帖子有多少页
      String pageSize = html.xpath("//*[@id=\"thread_theme_5\"]/div[1]/ul/li[2]/span[2]/text()")
          .toString();
      // 将帖子的下一页加入待爬
      int size = Integer.parseInt(pageSize == null ? "0" : pageSize);
      if (size >= 2) {
        for (int i = 2; i <= size; i++) {
          if (url.indexOf("pn") < 0) {
            String urlPost = url + "?pn=" + i;
            page.addTargetRequest(urlPost);
          }
        }
      }
      /**
       * 主题信息
       */
      Post post = new Post();
      String data = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/@data-field").get();
      PostUser postUser = null;
      if (StringUtils.isNotBlank(data)) {
        postUser = JSONObject.parseObject(data, PostUser.class);
      } else {
        // 获取不到信息
        return;
      }
      String time =
          html.xpath(
              "//*[@id=\"j_p_postlist\"]/div[1]/div[3]/div[3]/div[1]/ul[2]/li[2]/span/text()")
              .toString();
      String content = html
          .xpath("//*[@id=\"post_content_" + postUser.getContent().getPost_id() + "\"]/text()")
          .toString();
      String replyNum = html.xpath("//*[@id=\"thread_theme_5\"]/div[1]/ul/li[2]/span[1]/text()")
          .toString();
      String title = html.xpath("//*[@id=\"j_core_title_wrap\"]/div[2]/h1/a/text()").toString();
      String userNickName = html.xpath("//*[@id=\"j_p_postlist\"]/div[1]/div[2]/ul/li[3]/a/text()")
          .toString();
      String userHref = html.xpath("//*[@id=\"j_p_postlist\"]/div/div[2]/ul/li[3]/a/@href").get();
      String userName = postUser.getAuthor().getUser_name();
      // 保存数据
      post.setContent(SpiderStringUtils.xffReplace(content));
      post.setPostUrl(StringUtils.substringBefore(url, "?pn="));
      post.setReplyNum(Integer.parseInt(StringUtils.isBlank(replyNum) ? "0" : replyNum));
      post.setTime(DateConvertUtils
          .parse(postUser.getContent().getDate() == null ? time : postUser.getContent().getDate(),
              DateConvertUtils.DATE_TIME_NO_SS));
      post.setTitle(SpiderStringUtils.xffReplace(title));
      post.setType(1);
      post.setUserName(SpiderStringUtils.xffReplace(userName));
      // 帖子分页不再保存
      if (url.indexOf("pn") < 0) {
        page.putField("post", post);
        totalPost.incrementAndGet();
        /**
         * 用户主页加入队列
         */
        if (userHref != null) {
          String userHome = URLGeneratedUtil.generatePostURL(userHref);
          page.addTargetRequest(userHome);
        }
      }

      /**
       * 回复信息
       */
      commentData(page, html);

    } catch (Exception e) {
      log.error("crawlPost error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//" + uuid + ".html");
    }
  }


  private void commentData(Page page, Html html) {
    // 当前页回帖数量
    List<String> commentSize = html
        .xpath("//*[@id=\"j_p_postlist\"]/@class=l_post j_l_post l_post_bright/").all();
    if (CollectionUtils.isEmpty(commentSize) || commentSize.size() < 0) {
      return;
    }

    /**
     * 回复信息
     */
    List<Comment> listComment = new ArrayList<>();
    for (int i = 2; i <= commentSize.size(); i++) {
      String dataComment = html.xpath("//*[@id=\"j_p_postlist\"]/div[" + i + "]/@data-field")
          .toString();
      String userHref = html
          .xpath("//*[@id=\"j_p_postlist\"]/div[" + i + "]/div[2]/ul/li[3]/a/@href").get();
      /**
       * 用户主页加入队列
       */
      if (userHref != null) {
        String userHome = URLGeneratedUtil.generatePostURL(userHref);
        page.addTargetRequest(userHome);
      }
      //
      PostUser dataCommentPo = null;
      if (StringUtils.isNotBlank(dataComment)) {
        dataCommentPo = JSONObject.parseObject(dataComment, PostUser.class);
        //
        String contentComment = html.xpath(
            "//*[@id=\"post_content_" + dataCommentPo.getContent().getPost_id() + "\"]/text()")
            .toString();
        String userNameComment = html
            .xpath("//*[@id=\"j_p_postlist\"]/div[" + i + "]/div[2]/ul/li[3]/a/text()").toString();
        //
        Comment comment = new Comment();
        comment.setContent(SpiderStringUtils.xffReplace(contentComment));
        comment.setPostUrl(page.getUrl().toString());
        comment.setUserDevice(dataCommentPo.getContent().getOpen_type());
        comment.setTime(DateConvertUtils
            .parse(dataCommentPo.getContent().getDate(), DateConvertUtils.DATE_TIME_NO_SS));
        comment.setUserName(SpiderStringUtils.xffReplace(userNameComment));
        //
        listComment.add(comment);
      } else {
        continue;
      }
    }
    page.putField("listComment", listComment);
    totalComment.incrementAndGet();
  }

  /**
   * 帖子页 获取帖子数据
   */
  private void crawlUser(Page page, Html html) {
    String url = page.getRequest().getUrl();
    try {
      String bodyclass = html.xpath("/html/body/@class").get();
      /**
       * 某些用户被屏蔽
       */
      if (StringUtils.isNotBlank(bodyclass) && bodyclass.contains("404")) {
        return;
      }
      String userName = html.xpath("//*[@id=\"userinfo_wrap\"]/div[2]/div[3]/div/span[2]/text()")
          .toString();
      userName = StringUtils.substringAfter(userName, "用户名:");
      String fansCount = html.xpath("//*[@id=\"container\"]/div[2]/div[4]/h1/span/a/text()")
          .toString();
      String followCount = html.xpath("//*[@id=\"container\"]/div[2]/div[3]/h1/span/a/text()")
          .toString();
      String gender = html.xpath("//*[@id=\"userinfo_wrap\"]/div[2]/div[3]/div/span[1]/@class")
          .get();
      String tbAge = html
          .xpath("//*[@id=\"userinfo_wrap\"]/div[2]/div[3]/div/span[2]/span[2]/text()").toString();
      List<String> all = html.xpath("//*[@id=\"container\"]/div[1]/div/div[3]/ul/").all();
      String userHeadUrl = html.xpath("//*[@id=\"j_userhead\"]/a/img/@src").get();
      // 用户关注的贴吧
      List<String> userTiebs = html.xpath("//*[@id=\"forum_group_wrap\"]/").all();
      if (!CollectionUtils.isEmpty(userTiebs)) {
        List<UserTbs> userTbsList = new ArrayList<>();
        for (int i = 1; i <= userTiebs.size(); i++) {
          UserTbs userTbs = new UserTbs();
          String tbName = html.xpath("//*[@id=\"forum_group_wrap\"]/a[" + i + "]/span[1]/text()")
              .toString();
          if (StringUtils.isBlank(tbName)) {
            tbName = html.xpath("//*[@id=\"forum_group_wrap\"]/a[" + i + "]/span[2]/text()")
                .toString();
          }
          //
          String level = html.xpath("//*[@id=\"forum_group_wrap\"]/a[" + i + "]/span[2]/@class")
              .get();
          if (StringUtils.isBlank(level)) {
            level = html.xpath("//*[@id=\"forum_group_wrap\"]/a[3]/span[3]/@class").get();
          }
          //
          String levelInt = StringUtils.substringAfter(level, "forum_level lv");
          if (StringUtils.isBlank(levelInt)) {
            level = html.xpath("//*[@id=\"forum_group_wrap\"]/a[" + i + "]/span[4]/@class").get();
            levelInt = StringUtils.substringAfter(level, "forum_level lv");
          }
          // 实在获取不到用户关注贴吧名跳过
          if (StringUtils.isBlank(levelInt) || StringUtils.isBlank(tbName)) {
            continue;
          }

          userTbs.setTbLevel(Integer.parseInt(levelInt));
          userTbs.setTbName(tbName);
          userTbs.setUserName(SpiderStringUtils.xffReplace(userName));
          userTbsList.add(userTbs);
        }
        page.putField("userTbsList", userTbsList);
      }

      //
      User user = new User();
      user.setUserHomeUrl(page.getRequest().getUrl());
      user.setUserName(SpiderStringUtils.xffReplace(userName));
      user.setFollowCount(Integer.parseInt(StringUtils.isBlank(followCount) ? "0" : followCount));
      user.setFansCount(Integer.parseInt(StringUtils.isBlank(fansCount) ? "0" : fansCount));
      user.setGender("userinfo_sex userinfo_sex_male".equals(gender) ? 1 : 0);
      String numAge = StringUtils.substringBetween(tbAge, "吧龄:", "年");
      user.setTbAge(Double.valueOf(StringUtils.isBlank(numAge) ? "0" : numAge));
      user.setPostCount(CollectionUtils.isEmpty(all) ? 0 : all.size());
      user.setUserHeadUrl(userHeadUrl);
      //
      page.putField("user", user);
      totalUser.incrementAndGet();
    } catch (Exception e) {
      log.error("crawlUser error url {}", url, e);
      String uuid = UUID.randomUUID().toString();
      SpiderFileUtils.writeString2local(html.toString(), "E://tieb-spider//" + uuid + ".html");
    }
  }

  public static void main(String[] args) {
    Spider.create(new PostProcessor())//
        //http://tieba.baidu.com/f?kw=%E5%A4%AA%E5%8E%9F%E5%B7%A5%E4%B8%9A%E5%AD%A6%E9%99%A2&ie=utf-8
        .addUrl(
            "http://tieba.baidu.com/f?kw=%E5%A4%AA%E5%8E%9F%E7%90%86%E5%B7%A5%E5%A4%A7%E5%AD%A6&ie=utf-8")//
        .addPipeline(new ConsolePipeline())//
        .thread(1)//
        .run();
  }
}
