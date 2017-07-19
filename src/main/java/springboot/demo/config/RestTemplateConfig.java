package springboot.demo.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import springboot.demo.common.RestTemplateExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
class RestTemplateConfig {


    public RestTemplateExt restTemplate() {
        return newRestTemplate();
    }

    private RestTemplateExt newRestTemplate() {
        RestTemplateExt restTemplate = new RestTemplateExt();
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> item : converterList) {
            if (item.getClass() == StringHttpMessageConverter.class) {
                item = new StringHttpMessageConverter(StandardCharsets.UTF_8);
            }
            if (item.getClass() == MappingJackson2HttpMessageConverter.class) {
                MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) item;
                ObjectMapper mapper = converter.getObjectMapper();
                mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            }
        }
        return restTemplate;
    }
}


