package springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import springboot.demo.cache.SpringBeanHelper;
import springboot.demo.service.CustomInvocationSecurityMetadataSourceService;
import springboot.demo.service.MySecurityFilter;
import springboot.demo.service.SyAccessDecisionManager;

/**
 * Created by zhouwei on 2017/5/18.
 */
@SpringBootApplication
//@EnableAutoConfiguration(exclude = MySecurityFilter.class)
public class ApplicationRun {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(ApplicationRun.class, args);
        SpringBeanHelper.setApplicationContext(applicationContext);
    }

    /*@Bean
    public SyAccessDecisionManager syAccessDecisionManager() {
        return new SyAccessDecisionManager();
    }*/

   /* @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor(SyAccessDecisionManager syAccessDecisionManager) {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(new CustomInvocationSecurityMetadataSourceService());
        filterSecurityInterceptor.setAccessDecisionManager(syAccessDecisionManager);
        return filterSecurityInterceptor;
    }*/
}
