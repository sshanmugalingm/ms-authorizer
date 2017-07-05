package au.com.bglcorp.domain.redis;

import au.com.bglcorp.domain.jwt.TokenPayload;
import java.io.Serializable;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public class Token implements Serializable {

    String tokenKey;
    String salt;
    TokenPayload payload;

    public Token(String tokenKey, String salt, TokenPayload payload) {
        this.tokenKey = tokenKey;
        this.salt = salt;
        this.payload = payload;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public String getSalt() {
        return salt;
    }

    public TokenPayload getPayload() {
        return payload;
    }
}
