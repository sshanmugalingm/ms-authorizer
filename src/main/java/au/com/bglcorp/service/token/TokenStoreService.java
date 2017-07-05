package au.com.bglcorp.service.token;

import au.com.bglcorp.domain.jwt.TokenPayload;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public interface TokenStoreService {

    String createToken(String salt, TokenPayload payload);

    TokenPayload getValidPayload(String tokenValue);
}
