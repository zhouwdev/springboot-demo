package springboot.demo.model;

import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;

/**
 * Created by zhouwei on 2017/6/21.
 */
public class User {
    private Integer id;

    private String userName;

    private String pwd;

    private boolean isEnable;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<ConfigAttribute> getAuths() {
        return auths;
    }

    public void setAuths(Collection<ConfigAttribute> auths) {
        this.auths = auths;
    }

    public Collection<ConfigAttribute> auths;


}
