package com.megvii.spider.test.update;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.megvii.dzh.spider.SpiderApplication;
import com.megvii.dzh.spider.common.enums.WordDivideType;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.vo.NameValue;
import com.megvii.dzh.spider.domain.vo.PostGroupByMonthVo;
import com.megvii.dzh.spider.domain.vo.PostYears;
import com.megvii.dzh.spider.mapper.UserMapper;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IWordDivideService;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = {SpiderApplication.class})
@Slf4j
public class TestUpdateData {

  @Autowired
  private IPostService postService;
  @Autowired
  private ICommentService commentService;
  @Autowired
  private IUserService userService;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private IWordDivideService wordDivideService;

  private ExecutorService updateExecutor = Executors.newFixedThreadPool(300, new DefaultThreadFactory("updateExecutor"));

  /**
   * UPDATE `user` set province='上海' WHERE province LIKE '%上海%';
   * UPDATE `user` set province='台湾' WHERE province LIKE '%台湾%';
   * UPDATE `user` set province='北京' WHERE province LIKE '%北京%';
   * UPDATE `user` set province='吉林' WHERE province LIKE '%吉林%';
   * UPDATE `user` set province='四川' WHERE province LIKE '%四川%';
   * UPDATE `user` set province='宁夏' WHERE province LIKE '%宁夏%';
   * UPDATE `user` set province='安徽' WHERE province LIKE '%安徽%';
   * UPDATE `user` set province='山东' WHERE province LIKE '%山东%';
   * UPDATE `user` set province='广东' WHERE province LIKE '%广东%';
   * UPDATE `user` set province='新疆' WHERE province LIKE '%新疆%';
   * UPDATE `user` set province='江苏' WHERE province LIKE '%江苏%';
   * UPDATE `user` set province='江西' WHERE province LIKE '%江西%';
   * UPDATE `user` set province='河北' WHERE province LIKE '%河北%';
   * UPDATE `user` set province='河南' WHERE province LIKE '%河南%';
   * UPDATE `user` set province='浙江' WHERE province LIKE '%浙江%';
   * UPDATE `user` set province='湖北' WHERE province LIKE '%湖北%';
   * UPDATE `user` set province='湖南' WHERE province LIKE '%湖南%';
   * UPDATE `user` set province='福建' WHERE province LIKE '%福建%';
   * UPDATE `user` set province='西藏' WHERE province LIKE '%西藏%';
   * UPDATE `user` set province='贵州' WHERE province LIKE '%贵州%';
   * UPDATE `user` set province='辽宁' WHERE province LIKE '%辽宁%';
   * UPDATE `user` set province='重庆' WHERE province LIKE '%重庆%';
   * UPDATE `user` set province='陕西' WHERE province LIKE '%陕西%';
   * UPDATE `user` set province='黑龙江' WHERE province LIKE '%黑龙江%';
   * UPDATE `user` set province='甘肃' WHERE province LIKE '%甘肃%';
   * UPDATE `user` set province='澳门' WHERE province LIKE '%澳门%';
   * UPDATE `user` set province='海南' WHERE province LIKE '%海南%';
   * UPDATE `user` set province='天津' WHERE province LIKE '%天津%';
   * UPDATE `user` set province='云南' WHERE province LIKE '%云南%';
   * UPDATE `user` set province='地球' where province='未知';
   *
   * SELECT province,count(1) from `user` GROUP BY province;
   */
  @Test
  public void test() {
    PageInfo<User> pageInfo = userService.getUserPages(1, 10);
    int pages = pageInfo.getPages();
    for (int i = 1; i <= pages; i++) {
      log.info("--> 当前处理 {} 页",i);
      PageInfo<User> userPageInfo = userService.getUserPages(i, 10000);
      List<User> userList = userPageInfo.getList();
      for (User user : userList) {

        updateExecutor.submit(()->{
          Long id = user.getId();
          String province = user.getProvince();
          //去掉市
          if (province.indexOf("广西") > 0) {
            String UpdateProvince = "广西";
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setProvince(UpdateProvince);
            int update = userService.updateByPrimaryKeySelective(updateUser);
            log.info("广西 update {} --> {}", province, UpdateProvince);
          } else if (province.indexOf("新疆") > 0) {
            String UpdateProvince = "新疆";
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setProvince(UpdateProvince);
            int update = userService.updateByPrimaryKeySelective(updateUser);
            log.info("新疆 update {} --> {}", province, UpdateProvince);
          } else if (province.indexOf("香港") > 0) {
            String UpdateProvince = "香港";
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setProvince(UpdateProvince);
            int update = userService.updateByPrimaryKeySelective(updateUser);
            log.info("香港 update {} --> {}", province, UpdateProvince);
          } else if (province.indexOf("内蒙古") > 0) {
            String UpdateProvince = "内蒙古";
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setProvince(UpdateProvince);
            int update = userService.updateByPrimaryKeySelective(updateUser);
            log.info("内蒙古 update {} --> {}", province, UpdateProvince);
          }else if (province.indexOf("山西") > 0) {
            String UpdateProvince = "山西";
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setProvince(UpdateProvince);
            int update = userService.updateByPrimaryKeySelective(updateUser);
            log.info("山西 update {} --> {}", province, UpdateProvince);
          }
          else if (province.indexOf("市") > 0) {
            String UpdateProvince = StringUtils.substringBefore(province, "市");
            User updateUser = new User();
            updateUser.setId(id);
            updateUser.setProvince(UpdateProvince);
            int update = userService.updateByPrimaryKeySelective(updateUser);
            log.info("市 update {} --> {}", province, UpdateProvince);
          }
        });
      }
    }
    //
    while (true){
      int activeCount = ((ThreadPoolExecutor) updateExecutor).getActiveCount();
      log.info("--> activeCount {}",activeCount);
      if(activeCount==0){
        log.info("finished...");
        break;
      }
    }
  }
}
