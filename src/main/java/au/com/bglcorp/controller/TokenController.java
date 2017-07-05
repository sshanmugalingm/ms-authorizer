package au.com.bglcorp.controller;

import au.com.bglcorp.domain.token.TokenPayload;
import au.com.bglcorp.dto.TokenDetails;
import au.com.bglcorp.dto.UserDetails;
import au.com.bglcorp.exception.InvalidTokenException;
import au.com.bglcorp.service.authorization.AuthorizationService;
import au.com.bglcorp.service.token.TokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public String requestToken(@RequestBody UserDetails userDetails) {

        Assert.notNull(userDetails, "User details cannot be null");
        userDetails.validate();

        return tokenStoreService.createToken(userDetails.getSalt(), TokenPayload.newInstance(userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @RequestMapping(path = "/validateToken", method = RequestMethod.POST)
    public Boolean validateToken(@RequestBody TokenDetails tokenDetails) {

        Assert.notNull(tokenDetails, "Token details cannot be null");
        tokenDetails.validate();

        Optional<TokenPayload> tokenPayload = tokenStoreService.getValidPayload(tokenDetails.getToken());
        return authorizationService.isAuthorized(tokenPayload.orElseThrow(() -> new InvalidTokenException("Could not validate token")), tokenDetails);
    }

    @RequestMapping(path = "/removeTokens", method = RequestMethod.DELETE)
    public Boolean removeTokens(@RequestBody String token) {

        Assert.notNull(token, "Token cannot be null");
        tokenStoreService.deleteToken(token);

        return true;
    }
}
