package au.com.bglcorp.dto;

import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public class UserDetailsWrapper {

    String username;
    String salt;
    List<String> supportedAuthorities;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<String> getSupportedAuthorities() {
        return supportedAuthorities;
    }

    public void setSupportedAuthorities(List<String> supportedAuthorities) {
        this.supportedAuthorities = supportedAuthorities;
    }

    public void validate() {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(salt, "User Salt cannot be null");
        Assert.notNull(supportedAuthorities, "Supported Authorities cannot be null");
    }
}
