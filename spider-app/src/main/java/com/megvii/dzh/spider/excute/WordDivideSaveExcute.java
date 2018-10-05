package com.megvii.dzh.spider.excute;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.service.IWordDivideService;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WordDivideSaveExcute extends ExpandThread<WordDivide> {

  private IWordDivideService wordDivideService = SpringUtils.getBean(IWordDivideService.class);

  public WordDivideSaveExcute(ArrayBlockingQueue<WordDivide> arrayBlockingQueue) {
    super(arrayBlockingQueue);
  }

  @Override
  public RunService perform(WordDivide wordDivide) {
    try {
      // 另外线程入库
      wordDivideService.insert(wordDivide);
    } catch (Exception e) {
      log.error("perform error User {}", JSONObject.toJSONString(wordDivide), e);
    } finally {
    }
    return null;
  }

  @Override
  public void writeBack(ResultBackObject resultBackObject) {
  }


}
