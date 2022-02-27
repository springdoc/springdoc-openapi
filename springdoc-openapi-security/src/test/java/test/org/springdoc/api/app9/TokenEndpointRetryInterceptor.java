package test.org.springdoc.api.app9;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class TokenEndpointRetryInterceptor {

    private static final int MAX_RETRIES = 3;

    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.*(..))")
    public Object execute (ProceedingJoinPoint aJoinPoint) throws Throwable {
        int tts = 1000;
        for (int i=1; i<=MAX_RETRIES; i++) {
            try {
                return aJoinPoint.proceed();
            } catch (Exception e) {
                Thread.sleep(tts);
                tts=tts*2;
            }
        }
        throw new IllegalStateException("Could not execute: " + aJoinPoint.getSignature().getName());
    }

}
