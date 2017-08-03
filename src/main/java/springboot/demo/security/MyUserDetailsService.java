package springboot.demo.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhouwei on 2017/6/20.
 */
@Service("myUserDetailsService")
public class MyUserDetailsService  implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("User : admin");
        if(username==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User("admin", "123456",
                true, true, true, true, getGrantedAuthorities());//getGrantedAuthorities(user)
    }

    private List<GrantedAuthority> getGrantedAuthorities(){
        // 读取权限
        Collection<ConfigAttribute> auths = new ArrayList<ConfigAttribute>();
        List<GrantedAuthority> list = new ArrayList<>();
        // 这里需要从数据库里读取所有的权限点
        Collection aes = new ArrayList<>();
        aes.add("op:add");
        for(Object o : aes){
            System.out.println("UserProfile : "+o);
            list.add(new SimpleGrantedAuthority("ROLE_op:add"));
            list.add(new SimpleGrantedAuthority("ROLE_op:add2"));
        }
        return list;
    }
}
