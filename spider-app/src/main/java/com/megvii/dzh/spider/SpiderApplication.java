package com.megvii.dzh.spider;

import com.megvii.dzh.spider.common.config.BootConfig;
import com.megvii.dzh.spider.common.constant.Constant;
import com.megvii.dzh.spider.common.utils.SpringUtils;
import com.megvii.dzh.spider.webmagic.pipelines.PostDownloadPipeline;
import com.megvii.dzh.spider.webmagic.processors.PostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import tk.mybatis.spring.annotation.MapperScan;
import us.codecraft.webmagic.Spider;

@SpringBootApplication
@ServletComponentScan
@ComponentScan({"com.megvii"})
@MapperScan(basePackages = "com.megvii.dzh.spider.mapper")
@Slf4j
public class SpiderApplication extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String args[]) {
        SpringApplication.run(SpiderApplication.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>>>>spiderboot 启动成功!<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("--->开机服务执行的操作....");
        try {

        } catch (Exception e) {
            log.error("onApplicationEvent error", e);
        }
    }

}
