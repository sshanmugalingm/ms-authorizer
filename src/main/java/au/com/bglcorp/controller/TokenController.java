package au.com.bglcorp.controller;

import au.com.bglcorp.domain.jwt.TokenPayload;
import au.com.bglcorp.dto.TokenDetailsWrapper;
import au.com.bglcorp.dto.UserDetailsWrapper;
import au.com.bglcorp.service.authorization.AuthorizationService;
import au.com.bglcorp.service.token.TokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * Created by senthurshanmugalingm on 30/06/2017.
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    TokenStoreService tokenStoreService;

    @Autowired
    AuthorizationService authorizationService;

    @RequestMapping(path = "/requestToken", method = RequestMethod.POST)
    public String requestToken(@RequestBody UserDetailsWrapper userDetails) {

        Assert.notNull(userDetails, "User details cannot be null");
        userDetails.validate();

        return tokenStoreService.createToken(userDetails.getSalt(), new TokenPayload(userDetails.getUsername(), userDetails.getSupportedAuthorities()));
    }

    @RequestMapping(path = "/validateToken", method = RequestMethod.POST)
    public Boolean validateToken(@RequestBody TokenDetailsWrapper tokenDetails) {

        Assert.notNull(tokenDetails, "Token details cannot be null");
        tokenDetails.validate();

        TokenPayload tokenPayload = tokenStoreService.getValidPayload(tokenDetails.getToken());
        return authorizationService.isAuthorized(tokenPayload, tokenDetails.getAuthorities());
    }
}
