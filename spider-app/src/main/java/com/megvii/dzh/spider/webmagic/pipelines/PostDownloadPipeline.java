package com.megvii.dzh.spider.webmagic.pipelines;

import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.domain.po.Comment;
import com.megvii.dzh.spider.domain.po.Post;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.excute.CommentSaveExcute;
import com.megvii.dzh.spider.excute.PostSaveExcute;
import com.megvii.dzh.spider.excute.UserSaveExcute;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Slf4j
public class PostDownloadPipeline implements Pipeline {

  @Autowired
  private BootConfig bootConfig;

  @Override
  public void process(ResultItems resultItems, Task task) {
    String url = resultItems.getRequest().getUrl();
    for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
      if (entry.getKey().equals("post")) {
        Post post = (Post) entry.getValue();
        try {
          // 处理线程池
          bootConfig.getThreadPoolPost().putAnRun(post, PostSaveExcute.class);
        } catch (Exception e) {
          log.error("putAnRun post error {}", url, e);
        }
      } else if (entry.getKey().equals("listComment")) {
        List<Comment> listComment = (List<Comment>) entry.getValue();
        try {
          bootConfig.getThreadCommentDivide().putAnRun(listComment, CommentSaveExcute.class);
        } catch (Exception e) {
          log.error("putAnRun listComment error {}", url, e);
        }
      } else if (entry.getKey().equals("user")) {
        User user = (User) entry.getValue();
        try {
          // 另外线程入库
          bootConfig.getThreadUserDivide().putAnRun(user, UserSaveExcute.class);
        } catch (Exception e) {
          log.error("putAnRun error {}", url, e);
        }
      }
    }
  }

}
