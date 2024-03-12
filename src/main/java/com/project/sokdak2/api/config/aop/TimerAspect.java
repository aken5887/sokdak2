package com.project.sokdak2.api.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * author         : choi
 * date           : 2024-03-05
 */

@Slf4j
@Aspect
@Component
public class TimerAspect {
    @Pointcut("@annotation(com.project.sokdak2.api.config.annotation.Timer)")
    private void timer(){}
    @Around("timer()")
    public Object timerAdvisor(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String methodName = signature.getMethod().getName();
        log.debug("실행 메서드 : {}, 살행시간 : {} ms", methodName, totalTimeMillis);
        return result;
    }
}
