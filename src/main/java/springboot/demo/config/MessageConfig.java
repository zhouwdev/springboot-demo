package springboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import springboot.demo.cache.ResCodeMessageCache;


@Configuration
class MessageConfig {

    @Bean
    public ResCodeMessageCache loadMessage() throws Exception {
        return new ResCodeMessageCache(new PathMatchingResourcePatternResolver().getResources("classpath:message-resCode.xml"));
    }
}
