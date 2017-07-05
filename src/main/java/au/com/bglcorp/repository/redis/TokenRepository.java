package au.com.bglcorp.repository.redis;

import au.com.bglcorp.domain.redis.Token;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public interface TokenRepository {

    void saveToken(Token token);

    Token findToken(String tokenKey);

    void deleteToken(String tokenKey);

}
