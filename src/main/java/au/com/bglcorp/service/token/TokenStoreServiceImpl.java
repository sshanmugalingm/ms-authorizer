package au.com.bglcorp.service.token;

import au.com.bglcorp.domain.redis.Token;
import au.com.bglcorp.domain.jwt.TokenPayload;
import au.com.bglcorp.repository.redis.TokenRepository;
import au.com.bglcorp.util.AuthorizerUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Created by senthurshanmugalingm on 30/06/2017.
 */
public class TokenStoreServiceImpl implements TokenStoreService {

    private static final Log logger = LogFactory.getLog(TokenStoreServiceImpl.class);
    private static final String SUBJECT = "AuthToken";

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public String createToken(String salt, TokenPayload payload) {
        Assert.notNull(salt, "Salt cannot be empty");
        Assert.notNull(payload, "Payload cannot be empty");

        String token = constructToken(salt, payload);
        persistTokenDetails(token, salt, payload);
        return token;
    }

    private String constructToken(String salt, TokenPayload payload) {
        logger.debug("Create Token for " + payload.toString());

        return Jwts.builder()
                   .setSubject(AuthorizerUtil.buildJsonString(payload))
                   .signWith(SignatureAlgorithm.HS512, salt)
                   .compact();
    }

    private void persistTokenDetails(String token, String salt, TokenPayload payload) {
        tokenRepository.saveToken(new Token(token, salt, payload));
    }

    @Override
    public TokenPayload getValidPayload(String tokenValue) {
        Token token = tokenRepository.findToken(tokenValue);
        Assert.notNull(token, "Invalid Token");

        TokenPayload payload = getPayload(tokenValue, token.getSalt());
        if (!payload.equals(token.getPayload())) {
            throw new IllegalArgumentException("Invalid Token");
        }

        return payload;
    }

    private TokenPayload getPayload(String token, String salt) {
        return AuthorizerUtil.buildObjectFromJsonString(destructToken(token, salt), TokenPayload.class);
    }

    private String destructToken(String token, String salt) {
        return Jwts.parser()
                   .setSigningKey(salt)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }
}
