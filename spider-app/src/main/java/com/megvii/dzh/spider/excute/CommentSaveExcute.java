package com.megvii.dzh.spider.excute;

import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import com.megvii.dzh.perfrom.concurrent.thread.ExpandThread;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.po.WordDivide;
import com.megvii.dzh.spider.service.ICommentService;
import com.megvii.dzh.spider.service.IWordDivideService;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

@Slf4j
public class CommentSaveExcute extends ExpandThread<Object> {

  private ICommentService commentService = SpringUtils.getBean(ICommentService.class);

  private IWordDivideService wordDivideService = SpringUtils.getBean(IWordDivideService.class);

  public CommentSaveExcute(ArrayBlockingQueue<Object> arrayBlockingQueue) {
    super(arrayBlockingQueue);
  }

  @Override
  public RunService perform(Object obj) {
    try {
      List<Comment> listComment = (List<Comment>) obj;
      for (Comment comment : listComment) {
        // 另外线程入库
        commentService.insert(comment);
        // 保存分词
        String content = comment.getContent();
        if (StringUtils.isBlank(content)) {
          continue;
        }
        List<Word> list = WordSegmenter.seg(content);
        // 另外线程保存分词
        for (Word word : list) {
          if (word.getText().trim().matches("\\d++")) {
            continue;
          }
          WordDivide wordDivide = new WordDivide();
          wordDivide.setWord(word.getText());
          wordDivide.setType(3);
          wordDivideService.insert(wordDivide);
        }
      }

    } catch (Exception e) {
      log.error("perform error Comment {}", JSONObject.toJSONString(obj), e);
    } finally {
    }
    return null;
  }

  @Override
  public void writeBack(ResultBackObject resultBackObject) {
  }


}
