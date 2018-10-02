package com.megvii.dzh.perfrom.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
@Data
public class SystemConfig {

  @Value("${perform.queue.size:5000}")
  private int arrayBlockingQueueSize; // 定义下载队列长度

  @Value("${perform.thread.pool.size:30}")
  private int poolSize; // 线程池大小
}
