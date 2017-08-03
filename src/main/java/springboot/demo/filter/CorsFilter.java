package springboot.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springboot.demo.config.DomainConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter implements Filter {

    @Autowired
    private DomainConfig domainConfig;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        //接口直接跳到下个过滤器
        if (request.getRequestURI().indexOf("/api/") == -1) {
            chain.doFilter(req, res);
        } else {
            String refer = request.getHeader("Refer");
            String origin = request.getHeader("Origin");
            if (refer != null) {
                for (String it : domainConfig.domain) {
                    if (refer.indexOf(it) != -1) {
                        response.setHeader("Access-Control-Allow-Origin", it);
                    }
                }
            } else if (origin != null) {
                for (String it : domainConfig.domain) {
                    if (origin.indexOf(it) != -1) {
                        response.setHeader("Access-Control-Allow-Origin", it);
                    }
                }
            }

            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-auth-token, x-requested-with,Authorization,Origin, Accept, Content-Type,x-xsrf-token");
            if (request.getMethod() != "OPTIONS") {
                chain.doFilter(req, res);
            } else {
            }

        }
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }


    private String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) ip = str;
            break;
        }

        return ip;
    }
}
