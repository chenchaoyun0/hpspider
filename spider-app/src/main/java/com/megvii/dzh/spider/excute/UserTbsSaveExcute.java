package com.megvii.dzh.spider.excute;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.domain.po.UserTbs;
import com.megvii.dzh.spider.service.IUserTbsService;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserTbsSaveExcute extends ExpandThread<Object> {

  private IUserTbsService userTbsService = SpringUtils.getBean(IUserTbsService.class);

  public UserTbsSaveExcute(ArrayBlockingQueue<Object> arrayBlockingQueue) {
    super(arrayBlockingQueue);
  }

  @Override
  public RunService perform(Object obj) {
    try {
      List<UserTbs> userTbsList = (List<UserTbs>) obj;

      for (UserTbs userTbs : userTbsList) {
        userTbsService.insert(userTbs);
      }

    } catch (Exception e) {
      log.error("perform error userTbs {}", JSONObject.toJSONString(obj), e);
    } finally {
    }
    return null;
  }

  @Override
  public void writeBack(ResultBackObject resultBackObject) {
  }


}
