package springboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import springboot.demo.cache.SpringBeanHelper;

/**
 * Created by zhouwei on 2017/5/18.
 */
@SpringBootApplication
public class ApplicationRun {
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(ApplicationRun.class, args);
        SpringBeanHelper.setApplicationContext(applicationContext);
    }

}
