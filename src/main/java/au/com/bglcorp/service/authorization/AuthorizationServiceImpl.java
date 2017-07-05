package au.com.bglcorp.service.authorization;

import au.com.bglcorp.domain.jwt.TokenPayload;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public Boolean isAuthorized(TokenPayload payload, List<String> supportedAuthorities) {

        return supportedAuthorities.stream()
                                   .filter(authority -> payload.getAuthorities().contains(authority))
                                   .collect(Collectors.toList()).size() > 0;
    }
}
