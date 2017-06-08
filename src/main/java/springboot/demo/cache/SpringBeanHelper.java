package springboot.demo.cache;

import org.springframework.context.ApplicationContext;

/**
 * Spring的Bean缓存类
 */
public class SpringBeanHelper {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 根据bean的名称或者bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}