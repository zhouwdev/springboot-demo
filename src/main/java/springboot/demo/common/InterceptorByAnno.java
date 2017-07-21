package springboot.demo.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 * Created by AA on 2017/7/12.
 */
@Component
@Aspect
public class InterceptorByAnno {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(springboot.demo.common.AnnotationDemo)")
    private void pointCutMethod() {

    }

    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class[] ps = method.getParameterTypes();
        AnnotationDemo annotation = method.getAnnotation(AnnotationDemo.class);
        parse(joinPoint, annotation);
        return joinPoint.proceed();
    }


    private void parse(ProceedingJoinPoint joinPoint, AnnotationDemo annotationDemo){
        try{
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String[] ps = signature.getParameterNames();
            ExpressionParser parser = new SpelExpressionParser();
            EvaluationContext ec = new StandardEvaluationContext();

            Object[] args = joinPoint.getArgs();
            for(int i = 0; i < args.length; i++){
                String pa = ps[i];
                ec.setVariable(pa,args[i]);
                logger.info("参数：======="  + ps[i]);
            }
            String param = parser.parseExpression(annotationDemo.key()).getValue(ec, Object.class).toString();
            System.out.println("key: "+param);
            System.out.println("like: "+parser.parseExpression(annotationDemo.like()).getValue(ec, Object.class).toString());
        }
        catch (Exception e){
            logger.error("解析SpEL表达式异常",e);
        }
    }
}
