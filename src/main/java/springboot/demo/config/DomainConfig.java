package springboot.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties
public class DomainConfig {

    public void setDomain(List<String> domain) {
        this.domain = domain;
    }

    public List<String> getDomain() {
        return domain;
    }

    public List<String> domain;


}
