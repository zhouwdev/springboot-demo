package springboot.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * Created by zhouwei on 2017/6/21.
 */
//@Component
public class MySecurityFilter  extends AbstractSecurityInterceptor
        implements Filter {
    @Autowired
    private SyAccessDecisionManager syAccessDecisionManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomInvocationSecurityMetadataSourceService  mySecurityMetadataSource;

    @PostConstruct
    public void init() {
        super.setAuthenticationManager(authenticationManager);
        super.setAccessDecisionManager(syAccessDecisionManager);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation( request, response, chain );
        invoke(fi);
    }

    public void destroy() {
        System.out.println("filter===========================end");
    }

    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        System.out.println("filtergergetghrthetyetyetyetyj");
        return this.mySecurityMetadataSource;
    }

    public void invoke( FilterInvocation fi ) throws IOException, ServletException{
        System.out.println("filter..........................");
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try{
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }finally{
            super.afterInvocation(token, null);
        }

    }

    public void init( FilterConfig filterconfig ) throws ServletException{
        System.out.println("filter===========================");
    }
}
