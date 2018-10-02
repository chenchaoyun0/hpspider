package com.megvii.dzh.spider.common.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements ApplicationContextAware {
  private static final Logger log = LoggerFactory.getLogger(SpringUtils.class);
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("SpringUtils setApplicationContext............................................................");
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> cls) {
        return applicationContext==null?null:applicationContext.getBean(cls);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> cls) {
        return applicationContext==null?null:applicationContext.getBeansOfType(cls);
    }

    public static <T> T getBean(String name, Class<T> cls) {
        return applicationContext==null?null:applicationContext.getBean(name, cls);
    }

}
