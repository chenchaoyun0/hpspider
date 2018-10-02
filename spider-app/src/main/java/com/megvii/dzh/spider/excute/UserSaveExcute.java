package com.megvii.dzh.spider.excute;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.service.IUserService;
import com.megvii.dzh.spider.service.IWordDivideService;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

@Slf4j
public class UserSaveExcute extends ExpandThread<User> {

  private IUserService userService = SpringUtils.getBean(IUserService.class);

  private IWordDivideService wordDivideService = SpringUtils.getBean(IWordDivideService.class);


  public UserSaveExcute(ArrayBlockingQueue<User> arrayBlockingQueue) {
    super(arrayBlockingQueue);
  }

  @Override
  public RunService perform(User user) {
    try {
      // 另外线程入库
      userService.insert(user);
      // 保存分词
      String title = user.getUserName();
      List<Word> list = WordSegmenter.seg(title);
      // 另外线程保存分词
      for (Word word : list) {
        String text = word.getText();
        if (text.trim().matches("\\d++")) {
          continue;
        }
        WordDivide wordDivide = new WordDivide();
        wordDivide.setWord(text);
        wordDivide.setType(1);
        //
        wordDivideService.insert(wordDivide);
      }
    } catch (Exception e) {
      log.error("perform error User {}", JSONObject.toJSONString(user), e);
    } finally {
    }
    return null;
  }

  @Override
  public void writeBack(ResultBackObject resultBackObject) {
  }


}
