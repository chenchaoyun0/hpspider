package com.megvii.dzh.perfrom.concurrent.thread;

import java.util.concurrent.ArrayBlockingQueue;
import com.megvii.dzh.perfrom.bean.ResultBackObject;
import com.megvii.dzh.perfrom.component.run.RunService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExpandThread<T> extends Thread {

  ArrayBlockingQueue<T> arrayBlockingQueue = null;

  public ExpandThread(ArrayBlockingQueue<T> arrayBlockingQueue) {
    this.arrayBlockingQueue = arrayBlockingQueue;
  }

  @Override
  public void run() {
    // 1.获取队列中的数据，直到队列为空时
    while (arrayBlockingQueue.size() > 0) {
      // 编写回写对象
      ResultBackObject resultBackObject = new ResultBackObject();
      try {
        T t = arrayBlockingQueue.take();
        // 执行下载线程逻辑，优先执行子类逻辑
        RunService runService = perform(t);
        if (runService != null) {
          runService.run();
        }
      } catch (InterruptedException e) {
        log.error("run error",e);
        resultBackObject.setCode(200);
        resultBackObject.setSucceed(false);
        resultBackObject.setDescribe("执行异常:" + e.getMessage());

      } finally {
        writeBack(resultBackObject);
      }
    }
  }

  public abstract RunService perform(T t);

  public abstract void writeBack(ResultBackObject resultBackObject);

}
