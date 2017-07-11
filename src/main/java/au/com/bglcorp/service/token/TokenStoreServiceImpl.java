package au.com.bglcorp.service.token;

import au.com.bglcorp.domain.redis.Token;
import au.com.bglcorp.domain.token.TokenPayload;
import au.com.bglcorp.exception.InvalidTokenException;
import au.com.bglcorp.repository.redis.TokenRepository;
import au.com.bglcorp.util.JsonUtil;
import au.com.bglcorp.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by senthurshanmugalingm on 30/06/2017.
 */
public class TokenStoreServiceImpl implements TokenStoreService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public String createToken(String salt, TokenPayload payload) {

        String token = TokenUtil.constructToken(JsonUtil.buildJsonString(payload.getUsername()), salt);

        if (!tokenRepository.findByTokenKey(token).isPresent()) {
            tokenRepository.saveToken(new Token(token, salt, payload));
        }
        return token;
    }

    @Override
    public Optional<TokenPayload> getValidPayload(String tokenValue) {
        Optional<TokenPayload> payload = Optional.empty();
        Optional<Token> token = tokenRepository.findByTokenKey(tokenValue);

        if (token.isPresent()) {
            String username = JsonUtil.buildObjectFromJsonString(TokenUtil.destructToken(token.get().getSalt(), tokenValue), String.class);
            payload =  username.equals(token.get().getPayload().getUsername()) ? Optional.of(token.get().getPayload()) : Optional.empty();
        }

        return payload;
    }

    public Boolean deleteToken(String token) {
        tokenRepository.findByTokenKey(token).orElseThrow(() -> new InvalidTokenException("Could not validate token"));
        tokenRepository.deleteToken(token);
        return true;
    }

}
