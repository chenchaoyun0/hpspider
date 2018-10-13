package com.megvii.dzh.spider.controller;

import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.enums.WordDivideType;
import com.megvii.dzh.spider.common.utils.NumberUtils;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;
import com.megvii.dzh.spider.domain.vo.Record;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IWordDivideService;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/")
@Controller
@Slf4j
public class AnalysisController {

  @Autowired
  private BootConfig bootConfig;

  @Resource
  private IPostService postService;
  @Resource
  private ICommentService commentService;
  @Resource
  private IUserService userService;
  @Resource
  private IWordDivideService wordDivideService;


  @RequestMapping(value = "/")
  public String index(Model model) {
    log.info("===========index=============");
    long countPost = postService.getLastId();
    long countComment = commentService.getLastId();
    long countUser = userService.getLastId();
    model.addAttribute("countPost", countPost);
    model.addAttribute("countComment", countComment);
    model.addAttribute("countUser", countUser);
    return "index";
  }

  @RequestMapping(value = "/postTitleWordCloud")
  public String getPostTitleWord() {
    return "postTitleWordCloud";
  }

  /**
   * @param year
   * @param limit
   * @return
   */
  @RequestMapping(value = "/postTitleWord")
  @ResponseBody
  public List<NameValue> postTitleWord(int year, int limit) {
    List<NameValue> nameValuesByYear = postService.nameValuesByYear(year + "", limit);
    return nameValuesByYear;
  }

  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/commentWordCloud")
  public String commentWordCloud() {
    return "commentWordCloud";
  }

  /**
   * @param limit
   * @return
   */
  @RequestMapping(value = "/getCommentWordCloud")
  @ResponseBody
  public List<NameValue> getCommentWordCloud(int limit) {
    return wordDivideService.nameValues(WordDivideType.CONTENT, limit);
  }

  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/activeUser")
  public String activeUser() {
    return "activeUser";
  }

  @RequestMapping(value = "/activeUser2016")
  public String activeUser2016() {
    return "activeUser2016";
  }

  @RequestMapping(value = "/activeUser2017")
  public String activeUser2017() {
    return "activeUser2017";
  }

  @RequestMapping(value = "/activeUser2018")
  public String activeUser2018() {
    return "activeUser2018";
  }

  /**
   * 20大活跃用户，按年分组
   */
  @RequestMapping(value = "/getActiveUser")
  @ResponseBody
  public List<NameValue> getActiveUser(int year, int limit) {
    List<NameValue> nameValuesByYear = commentService.getActiveUser(year, limit);
    return nameValuesByYear;
  }

  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/activeUserBar")
  public String activeUserBar() {
    return "activeUserBar";
  }

  /**
   * 20大活跃用户，按年分组
   */
  @RequestMapping(value = "/getActiveUserBar")
  @ResponseBody
  public List<NameValue> getActiveUserBar(int limit) {
    List<NameValue> nameValuesByYear = commentService.getActiveUserBar(limit);
    return nameValuesByYear;
  }

  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/userFansBar")
  public String userFansBar() {
    return "userFansBar";
  }

  /**
   * 粉丝最多的10大用户
   */
  @RequestMapping(value = "/getUserFansBar")
  @ResponseBody
  public List<NameValue> getUserFansBar(int limit) {
    return userService.getUserFansBar(limit);
  }

  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/postUserTopBar")
  public String postUserTopBar() {
    return "postUserTopBar";
  }

  /**
   * 十大发帖量最多的用户
   */
  @RequestMapping(value = "/getPostUserTopBar")
  @ResponseBody
  public List<NameValue> getPostUserTopBar(int limit) {
    return postService.getPostUserTopBar(limit);
  }
  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 发帖用户与不发帖比例
   */
  @RequestMapping(value = "/postAndNo")
  public String postAndNo() {
    return "postAndNo";
  }

  @RequestMapping(value = "/getPostAndNo")
  @ResponseBody
  public List<NameValue> getPostAndNo() {
    List<NameValue> result = postService.getPostAndNo();
    return result;
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 有回复帖子与没回复比例
   */
  @RequestMapping(value = "/postHasRepAndNo")
  public String postHasRepAndNo() {
    return "postHasRepAndNo";
  }

  @RequestMapping(value = "/getPostHasRepAndNo")
  @ResponseBody
  public List<NameValue> getPostHasRepAndNo() {
    List<NameValue> result = postService.getPostHasRepAndNo();
    return result;
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 测试曲线图
   */
  @RequestMapping(value = "/demo")
  public String demo() {
    return "demo";
  }

  @RequestMapping(value = "/getDemo")
  @ResponseBody
  public List<Record> getDemo() {
    List<Record> records = new ArrayList<Record>();
    for (int i = 0; i < 100; i++) {
      Record record = new Record();
      record.setDate(new Timestamp(System.currentTimeMillis()));
      record.setHum(NumberUtils.randomInt(10) + "");
      record.setPa(NumberUtils.randomInt(10) + "");
      record.setRain(NumberUtils.randomInt(10) + "");
      record.setTaizhan_num("A0001");
      record.setTem(NumberUtils.randomInt(10) + "");
      record.setWin_dir(NumberUtils.randomInt(10) + "");
      record.setWin_sp(NumberUtils.randomInt(10) + "");
      //将时间转换成给定格式便于echarts的X轴日期坐标显示
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String str = sdf.format(record.getDate());
      record.setDateStr(str);
      records.add(record);
    }

    return records;
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 年发帖量曲线图
   */
  @RequestMapping(value = "/postYears")
  public String postYears() {
    return "postYears";
  }

  @RequestMapping(value = "/postMonth")
  public String postMonth() {
    return "postMonth";
  }

  @RequestMapping(value = "/postHours")
  public String postHours() {
    return "postHours";
  }

  @RequestMapping(value = "/getPostGroupBy")
  @ResponseBody
  public List<PostYears> getPostGroupBy(String groupBy) {
    return postService.getPostGroupBy(groupBy);
  }

  @RequestMapping(value = "/getPostGroupByMonth")
  @ResponseBody
  public List<PostGroupByMonthVo> getPostGroupByMonth() {
    return postService.getPostGroupByMonth();
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * NBA主队分布
   */
  @RequestMapping(value = "/affiliationPie")
  public String affiliationPie() {
    return "affiliationPie";
  }

  @RequestMapping(value = "/getAffiliationPie")
  @ResponseBody
  public List<NameValue> getAffiliationPie() {
    return userService.getAffiliationPie();
  }

  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/userLevel")
  public String userLevel() {
    return "userLevel";
  }

  public List<NameValue> getUserLevel() {
    return userService.getUserLevel();
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 年度的十大热帖
   */
  @RequestMapping(value = "/getPostTitlesyear")
  @ResponseBody
  public List<NameValue> getPostTitlesyear(String year) {
    List<NameValue> result = postService.getPostTitlesyear(year);
    return result;
  }

  @RequestMapping(value = "/postTitlesyear2015")
  public String postTitlesyear2015() {
    return "postTitlesyear2015";
  }

  @RequestMapping(value = "/postTitlesyear2016")
  public String postTitlesyear2016() {
    return "postTitlesyear2016";
  }

  @RequestMapping(value = "/postTitlesyear2017")
  public String postTitlesyear2017() {
    return "postTitlesyear2017";
  }

  @RequestMapping(value = "/postTitlesyear2018")
  public String postTitlesyear2018() {
    return "postTitlesyear2018";
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  @RequestMapping(value = "/userNameWordCloud")
  public String userNameWordCloud() {
    return "userNameWordCloud";
  }

  /**
   * 用户贴吧名词云
   */
  @RequestMapping(value = "/getUserNameWordCloud")
  @ResponseBody
  public List<NameValue> getUserNameWordCloud(int limit) {
    return wordDivideService.nameValues(WordDivideType.USER_NAME, limit);
  }

  @RequestMapping(value = "/tbNameWordCloud")
  public String tbNameWordCloud() {
    return "tbNameWordCloud";
  }


  // ------------------------------***我是分割线***--------------------------------------------//
  @RequestMapping(value = "/getUserGender")
  @ResponseBody
  public List<NameValue> getUserGender() {
    return userService.getUserGender();
  }

  /**
   * 男女比例
   */
  @RequestMapping(value = "/userGender")
  public String userGender() {
    return "userGender";
  }

  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 吧龄分布
   */
  @RequestMapping(value = "/usertbAge")
  public String usertbAge() {
    return "usertbAge";
  }


  @RequestMapping(value = "/getUsertbAge")
  @ResponseBody
  public List<NameValue> getUsertbAge(int limit) {
    return userService.getUsertbAge(limit);
  }
  // ------------------------------***我是分割线***--------------------------------------------//

  /**
   * 用户设备分布
   */
  @RequestMapping(value = "/userDevicePie")
  public String userDevicePie() {
    return "userDevicePie";
  }


  @RequestMapping(value = "/getUserDevicePie")
  @ResponseBody
  public List<NameValue> getUserDevicePie(int limit) {
    return commentService.getUserDevicePie(limit);
  }

}
