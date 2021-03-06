package au.com.bglcorp.repository.redis;

import au.com.bglcorp.domain.redis.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private static final String KEYNAME = "AUTHKEY";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveToken(Token token) {
        hashOperations.put(KEYNAME, token.getTokenKey(), token);
    }

    @Override
    public Optional<Token> findByTokenKey(String tokenKey) {
        return Optional.ofNullable((Token) hashOperations.get(KEYNAME, tokenKey));
    }

    @Override
    public void deleteToken(String tokenKey) {
        hashOperations.delete(KEYNAME, tokenKey);
    }

}
