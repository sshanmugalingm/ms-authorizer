package au.com.bglcorp.domain.token;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by senthurshanmugalingm on 30/06/2017.
 */
public class TokenPayload implements Serializable {

    String username;
    Map<String, List<String>> authorities;

    public static TokenPayload newInstance(String username, Map<String, List<String>> authorities) {
        return new TokenPayload(username, authorities);
    }

    private TokenPayload(String username, Map<String, List<String>> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public Map<String, List<String>> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TokenPayload)) return false;

        TokenPayload payload = (TokenPayload) o;

        return new EqualsBuilder()
                .append(username, payload.username)
                .append(authorities, payload.authorities)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(authorities)
                .toHashCode();
    }
}
