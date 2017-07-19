package springboot.demo.common;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RestTemplateExt extends RestTemplate {

    public RestTemplateExt() {
        super();
    }

    public <T> T getForObject(String url, Object request, Class<T> responseType, Map<String, String> urlVariables)
            throws Exception {
        RequestCallback requestCallback = httpEntityCallback(request, responseType);
        HttpMessageConverterExtractor<T> responseExtractor = new HttpMessageConverterExtractor<>(responseType, getMessageConverters());
        URI expanded = new URI(expandedUrl(url, urlVariables));
        return execute(expanded, HttpMethod.GET, requestCallback, responseExtractor);
    }

    String expandedUrl(String url, Map<String, String> params) {
        if (url == null || url.isEmpty())
            return "";
        if (params == null || params.isEmpty()) {
            return url;
        }

        String[] urls = url.split("\\?");
        if (urls.length == 1) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = params.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    sb.append(key + "=" + URLEncoder.encode(params.get(key), "utf-8") + "&");
                } catch (Exception e) {
                }
            }

            if (sb.length() != 0) sb.deleteCharAt(sb.length() - 1);
            return url + "?" + sb.toString();
        } else {
            Map<String, String> paramMap = new LinkedHashMap<>();
            String[] urlParams = urls[1].split("&");
            for (String urlParam : urlParams) {
                String[] values = urlParam.split("=");
                paramMap.put(values[0], values[1]);
            }
            if (params != null) {
                paramMap.putAll(params);
            }
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = paramMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                try {
                    sb.append(key + "=" + paramMap.get(key) + "&");
                } catch (Exception e) {
                }
            }

            if (sb.length() != 0) sb.deleteCharAt(sb.length() - 1);
            return urls[0] + "?" + sb.toString();
        }
    }
}
