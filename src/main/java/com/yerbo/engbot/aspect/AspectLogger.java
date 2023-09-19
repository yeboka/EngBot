package com.yerbo.engbot.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.telegram.telegrambots.meta.api.objects.Update;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectLogger {

    private static final Logger logger = LoggerFactory.getLogger(AspectLogger.class);

    @Pointcut("within(com.yerbo.engbot.services.OpenAiApiService)")
    public void openAiServiceAspect(){}

    @Pointcut("within(com.yerbo.engbot.services.GoogleTranslateService)")
    public void googleTranslateServiceAspect(){}
    
    @Pointcut("within(com.yerbo.engbot.services.BotService)")
    public void botServiceAspect(){}

    @Around("openAiServiceAspect()")
    public Object aroundGetDefinitionOfWord(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Getting definition of word");
        StopWatch sw = new StopWatch();
        sw.start();
        Object res = joinPoint.proceed();
        sw.stop();
        logger.info("open ai return response in {}ms", sw.getTotalTimeMillis());
        return res;
    }

    @Around("googleTranslateServiceAspect()")
    public Object aroundGoogleTranslateMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Translating word...");
        StopWatch sw = new StopWatch();
        sw.start();
        Object res = joinPoint.proceed();
        sw.stop();
        logger.info("Google translate return response in {}ms", sw.getTotalTimeMillis());
        return res;
    }

    @AfterReturning(pointcut = "googleTranslateServiceAspect()", returning = "result")
    public void afterGoogleTranslateMethod(String[] result) {
        logger.info("Translating result: {} - {}", result[0], result[1]);
    }
    
    @Before(value = "botServiceAspect() && args(chatId, username)", argNames = "chatId,username")
    public void beforeUpdate(Long chatId, String username) {
        logger.info("User {} with id {} send message", username, chatId);
    }

    
}
