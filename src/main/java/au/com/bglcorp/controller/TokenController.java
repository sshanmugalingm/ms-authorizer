package au.com.bglcorp.controller;

import au.com.bglcorp.domain.token.TokenPayload;
import au.com.bglcorp.dto.Result;
import au.com.bglcorp.dto.TokenDetails;
import au.com.bglcorp.dto.UserDetails;
import au.com.bglcorp.exception.InvalidTokenException;
import au.com.bglcorp.exception.UnAuthorizedAccessException;
import au.com.bglcorp.service.authorization.AuthorizationService;
import au.com.bglcorp.service.token.TokenStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Result<String> requestToken(@RequestBody UserDetails userDetails) {

        userDetails.validate();
        return new Result<>(tokenStoreService.createToken(userDetails.getSalt(), TokenPayload.newInstance(userDetails.getUsername(), userDetails.getAuthorities())));
    }

    @RequestMapping(path = "/validateToken", method = RequestMethod.POST)
    public Result<Boolean> validateToken(@RequestBody TokenDetails tokenDetails) {

        tokenDetails.validate();
        Optional<TokenPayload> tokenPayload = tokenStoreService.getValidPayload(tokenDetails.getToken());
        Boolean result = authorizationService.isAuthorized(tokenPayload.orElseThrow(() -> new InvalidTokenException("Could not validate token")), tokenDetails);
        if (!result) {
                throw new UnAuthorizedAccessException("No Authority found for this request");
        }

        return new Result<>(result);
    }

    @RequestMapping(path = "/removeToken/{token:.+}", method = RequestMethod.DELETE)
    public Result<Boolean> removeToken(@PathVariable String token) {

        tokenStoreService.deleteToken(token);
        return new Result<>(Boolean.TRUE);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IllegalArgumentException.class)
    private void validationFailed() {}
}
