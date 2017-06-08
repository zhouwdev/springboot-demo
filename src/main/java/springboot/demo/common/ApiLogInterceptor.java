package springboot.demo.common;

import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import springboot.demo.cache.ResCodeMessageCache;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

@Component
@Aspect
class ApiLogInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("unused")
    @Pointcut("execution(* springboot.demo.api..*.*(..))")
    private void pointCutMethod() {
    }

    //声明环绕通知
    @Around("pointCutMethod()")
    Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder sb = new StringBuilder("\r\n");
        try {
            Class<?> classTarget = joinPoint.getTarget().getClass();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            ApiOperation annotation = method.getAnnotation(ApiOperation.class);
            sb.append("[方法]" + classTarget + "." + signature.getName().toString());
            if (annotation != null) {
                sb.append(" " + annotation.value());
            }
            sb.append("\r\n");
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                sb.append("[参数]");
                for (Object obj : joinPoint.getArgs()) {
                    try {
                        if (obj instanceof MultipartFile) {
                            MultipartFile file = (MultipartFile) obj;
                            sb.append(file.getOriginalFilename() + "[" + (file.getSize() / 1024) + "KB]");
                        } else if (obj instanceof File) {
                            File file = (File) obj;
                            sb.append(file.getName() + "[" + (file.length() / 1024) + "KB],");
                        } else if (obj instanceof ServletRequest || obj instanceof ServletResponse) {
                            sb.append(obj.getClass() + ",");
                        } else {
                            sb.append(JsonUtils.toJson(obj) + ",");
                        }
                    } catch (Exception e) {
                        //转json异常后直接toString
                        sb.append(obj == null ? "null" : obj + ",");
                    }
                }
                int index = sb.lastIndexOf(",");
                if (index > 0) {
                    sb.replace(index, index + 1, "");
                }
                sb.append("\r\n");
            }
            long begin = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            sb.append("[返回]" + (result == null ? "null" : JsonUtils.toJson(result)) + "\r\n");
            sb.append("[耗时]" + (end - begin) + " ms\r\n");
            logger.info(sb.toString());
            return result;
        } catch (Exception e) {
            if (e instanceof UndeclaredThrowableException) {
                e = getRealException((UndeclaredThrowableException) e);
            }
            sb.append("[异常]");
            if (e instanceof ApiException) {
                sb.append(((ApiException) e).resCode + ":" + ((ApiException) e).resDesc);
            } else {
                if (e.getMessage() != null) {
                    String desc = ResCodeMessageCache.getMessageDesc(e.getMessage());
                    if (desc != null) {
                        sb.append(e.getMessage() + ":" + desc);
                    }
                } else {
                    sb.append(e.getMessage());
                }
            }
            logger.warn(sb.toString());
            throw e;
        }
    }

    Exception getRealException(UndeclaredThrowableException e) {
        if (e == null) {
            return null;
        }
        Throwable cause = e.getCause();
        if (cause instanceof UndeclaredThrowableException) {
            return getRealException((UndeclaredThrowableException) cause);
        }
        return (Exception) cause;
    }
}
