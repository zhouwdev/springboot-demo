package springboot.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;


public class AuthenticationProviderCustom implements AuthenticationProvider{

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Override
    public  Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        //根据前端组合规则拆分获取用户名密码等信息
        Integer splitIndex = token.getName().lastIndexOf("$");

        String userName = null;
        String channelCode = null;
        if (splitIndex != -1){
            userName = token.getName().substring(0,splitIndex);
            channelCode = token.getName().substring(splitIndex+1);
        }else{
            userName = token.getName();
        }

        org.springframework.security.core.userdetails.User user = (User)myUserDetailsService.loadUserByUsername(userName);

        return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
       return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
