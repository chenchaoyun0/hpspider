package com.megvii.spider.test.spider;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MySitePipeline implements Pipeline {

  @Override
  public void process(ResultItems resultItems, Task task) {
    for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
      if (entry.getKey().equals("ipCountList")) {
        List<IpCount> ipCountList = ( List<IpCount>)entry.getValue();
        if (ipCountList != null) {
          System.out.println("----------> "+JSONObject.toJSONString(ipCountList));
        }
      }
    }
  }

}
