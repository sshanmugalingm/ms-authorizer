package au.com.bglcorp.service.authorization;

import au.com.bglcorp.domain.jwt.TokenPayload;

import java.util.List;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public interface AuthorizationService {

    Boolean isAuthorized(TokenPayload payload, List<String> supportedAuthorities);

}
