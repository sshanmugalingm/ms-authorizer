package au.com.bglcorp.domain.jwt;

import java.io.Serializable;
import java.util.List;

/**
 * Created by senthurshanmugalingm on 30/06/2017.
 */
public class TokenPayload implements Serializable {

    String username;
    List<String> authorities;

    public TokenPayload(String username, List<String> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "JwtPayload{" +
                "username='" + username + '\'' +
                ", authorities='" + authorities.toString() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TokenPayload)) return false;

        TokenPayload payload = (TokenPayload) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(username, payload.username)
                .append(authorities, payload.authorities)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(username)
                .append(authorities)
                .toHashCode();
    }
}
