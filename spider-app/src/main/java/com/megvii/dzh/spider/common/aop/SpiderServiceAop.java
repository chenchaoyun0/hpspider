package com.megvii.dzh.spider.common.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
@Slf4j
public class SpiderServiceAop {


  /**
   * 指定切方法
   */
  @Pointcut("execution(* com.megvii.dzh.spider.service..*.*(..))")
  public void access() {
  }

  @Before("access()")
  public void deBefore(JoinPoint joinPoint) {
    // 记录下请求内容
    log.debug("请求class_method : {}",
        joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    log.debug("请求参数args : {}", Arrays.toString(joinPoint.getArgs()));
  }

  @AfterReturning(returning = "ret", pointcut = "access()")
  public void doAfterReturning(Object ret) {
    // 处理完请求，返回内容
    log.debug("<<<<<<<<<<<<<<<<<<<< 方法执行 end...返回值 : {}");
  }

  /**
   * 后置异常通知
   */
  @AfterThrowing("access()")
  public void throwss(JoinPoint jp) {
    log.debug("@AfterThrowing 方法异常时执行.....");
  }

  /**
   * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
   */
  @After("access()")
  public void after(JoinPoint jp) {
    //log.debug("@After 方法最后执行.....");
  }

  /**
   * 环绕通知,环绕增强，相当于MethodInterceptor
   */
  @Around("access()")
  public Object arround(ProceedingJoinPoint pjp) {
    log.debug(">>>>>>>>>>>>>>>>>>>> 方法执行 begin...");
    try {
      Object o = pjp.proceed();
      //log.debug("方法环绕 @Around end...结果:{}");
      return o;
    } catch (Throwable e) {
      log.error("异常:{}", e);
      return null;
    }
  }
}
