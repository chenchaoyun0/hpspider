package com.megvii.dzh.spider.excute;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.service.IPostService;
import com.megvii.dzh.spider.service.IWordDivideService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

@Slf4j
public class PostSaveExcute extends ExpandThread<Post> {

  private IPostService postService = SpringUtils.getBean(IPostService.class);

  private BootConfig bootConfig = SpringUtils.getBean(BootConfig.class);

  public PostSaveExcute(ArrayBlockingQueue<Post> arrayBlockingQueue) {
    super(arrayBlockingQueue);
  }

  @Override
  public RunService perform(Post post) {
    try {
      Date time = post.getTime();
      Calendar cal = Calendar.getInstance();
      cal.setTime(time);
      post.setYear(cal.get(Calendar.YEAR));
      post.setMonth(cal.get(Calendar.MONTH)+1);
      post.setDay(cal.get(Calendar.DATE));
      post.setHour(cal.get(Calendar.HOUR_OF_DAY));
      // 另外线程入库
      postService.insert(post);
      // 保存分词
      String title = post.getTitle();
      if(StringUtils.isBlank(title)){
        return null;
      }
      List<Word> list = WordSegmenter.seg(title);
      // 另外线程保存分词
      for (Word word : list) {
        String text = word.getText();
        if (text.trim().matches("\\d++")) {
          continue;
        }
        WordDivide wordDivide = new WordDivide();
        wordDivide.setWord(text);
        wordDivide.setType(2);
        //
        bootConfig.getWordDivideSaveExcute().putAnRun(wordDivide,WordDivideSaveExcute.class);
      }

    } catch (Exception e) {
      log.error("perform error post {}", JSONObject.toJSONString(post), e);
    } finally {
    }
    return null;
  }

  @Override
  public void writeBack(ResultBackObject resultBackObject) {
  }


}
