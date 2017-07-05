package au.com.bglcorp.dto;

import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public class UserDetails {

    String username;
    String salt;
    Map<String, List<String>> authorities;

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

    public Map<String, List<String>> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Map<String, List<String>> authorities) {
        this.authorities = authorities;
    }

    public void validate() {
        Assert.notNull(username, "Username cannot be null");
        Assert.notNull(salt, "User Salt cannot be null");
        Assert.notNull(authorities, "Firm Authorities cannot be null");
    }
}
