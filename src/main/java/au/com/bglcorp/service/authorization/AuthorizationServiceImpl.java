package au.com.bglcorp.service.authorization;

import au.com.bglcorp.domain.token.TokenPayload;
import au.com.bglcorp.dto.TokenDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public Boolean isAuthorized(TokenPayload payload, TokenDetails tokenDetails) {

        List<String> applicableAuthorities = payload.getAuthorities().get(tokenDetails.getFirmShortName());
        return applicableAuthorities != null && applicableAuthorities.size() > 0 && tokenDetails.getAuthorities().stream()
                                                                                               .filter(authority -> applicableAuthorities.contains(authority))
                                                                                               .collect(Collectors.toList()).size() > 0;
    }
}
