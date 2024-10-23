package kr.kro.gonggibap.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("within(kr.kro.gonggibap.domain.*.controller..*)")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

        // 메서드 정보 가져오기
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 파라미터 정보 가져오기
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);

        log.info("====== 시작: {}.{} :: parameters {}", className, methodName, params);
        double start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed(args);
            return result;
        } finally {
            double executionTime = (System.currentTimeMillis() - start) / 1000;
            log.info("====== 종료: {}.{} :: executionTime {}", className, methodName, executionTime);
        }
    }

}
