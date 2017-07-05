package au.com.bglcorp.repository.redis;

import au.com.bglcorp.domain.redis.Token;

import java.util.Optional;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public interface TokenRepository {

    void saveToken(Token token);

    Optional<Token> findByTokenKey(String tokenKey);

    void deleteToken(String tokenKey);

}
