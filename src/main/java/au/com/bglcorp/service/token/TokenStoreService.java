package au.com.bglcorp.service.token;

import au.com.bglcorp.domain.token.TokenPayload;

import java.util.Optional;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public interface TokenStoreService {

    String createToken(String salt, TokenPayload payload);

    Optional<TokenPayload> getValidPayload(String tokenValue);

    Boolean deleteToken(String token);
}
