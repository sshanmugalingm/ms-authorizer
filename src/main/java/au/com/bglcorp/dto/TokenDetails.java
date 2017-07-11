package au.com.bglcorp.dto;

import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by senthurshanmugalingm on 5/07/2017.
 */
public class TokenDetails {

    String token;
    String firmShortName;
    List<String> authorities;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getFirmShortName() {
        return firmShortName;
    }

    public void setFirmShortName(String firmShortName) {
        this.firmShortName = firmShortName;
    }

    public void validate() {
        Assert.notNull(token, "Token cannot be null");
        Assert.notNull(firmShortName, "Firm Short cannot Name be null");
        Assert.notNull(authorities, "Supported Authorities cannot be null");
    }
}
