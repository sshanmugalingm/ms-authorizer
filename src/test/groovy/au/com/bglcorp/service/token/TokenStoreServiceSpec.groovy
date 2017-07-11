package au.com.bglcorp.service.token

import au.com.bglcorp.domain.redis.Token
import au.com.bglcorp.domain.token.TokenPayload
import au.com.bglcorp.repository.redis.TokenRepository
import au.com.bglcorp.util.JsonUtil
import au.com.bglcorp.util.TokenUtil
import spock.lang.Specification


/**
 * Created by senthurshanmugalingm on 7/07/2017.
 */
class TokenStoreServiceSpec extends Specification {

    TokenStoreService tokenStoreService

    def setup() {
        tokenStoreService = new TokenStoreServiceImpl()
    }

    def "createToken, should return a valid JWT token and save the token to the underlying token store, when a Valid Salt and Token Payload are provided"() {
        given :
        TokenPayload tokenPayload = TokenPayload.newInstance('abc@example.com', ["firm": ["AUTH_T"]])
        String salt = 'salt'

        tokenStoreService.tokenRepository = Mock(TokenRepository) {
            1 * findByTokenKey(*_) >> {String token ->
                return Optional.empty()
            }

            1 * saveToken(*_) >> {Token token -> }
        }

        when :
        String token = tokenStoreService.createToken(salt, tokenPayload)

        then :
        token
    }

    def "createToken, should return a valid JWT token and avoid saving the token to the token store, when a Valid Salt and Token Payload are provided and the Token exists in the underlying token store"() {
        given :
        TokenPayload tokenPayload = TokenPayload.newInstance('abc@example.com', ["firm": ["AUTH_T"]])
        String salt = 'salt'

        tokenStoreService.tokenRepository = Mock(TokenRepository) {
            1 * findByTokenKey(*_) >> {String token ->
                return Optional.of(new Token('tokenKey', salt, tokenPayload))
            }

            0 * saveToken(*_) >> {Token token -> }
        }

        when :
        String token = tokenStoreService.createToken(salt, tokenPayload)

        then :
        token
    }

    def "getValidPayload, should return empty payload, when the Token cannot be found in the token store"() {
        given:
        tokenStoreService.tokenRepository = Mock(TokenRepository) {
            1 * findByTokenKey(*_) >> {String token ->
                return Optional.empty()
            }
        }

        expect:
        !tokenStoreService.getValidPayload(UUID.randomUUID().toString()).isPresent()
    }

    def "getValidPayload, should return empty payload, when the Token is not valid"() {
        given:
        String constructedToken = TokenUtil.constructToken(JsonUtil.buildJsonString('abc@example.com'), 'salt')
        Token token = new Token(constructedToken, 'salt', TokenPayload.newInstance('abc@example.com', ['testFirm' : ['AUTH_USER']]))

        tokenStoreService.tokenRepository = Mock(TokenRepository) {
            1 * findByTokenKey(*_) >> {String strToken ->
                return Optional.of(token)
            }
        }

        expect:
        !tokenStoreService.getValidPayload(TokenUtil.constructToken(JsonUtil.buildJsonString(UUID.randomUUID().toString()), 'salt')).isPresent()
    }

    def "getValidPayload, should return valid payload, when the Token exist in the token store and the token is valid"() {
        given:
        String constructedToken = TokenUtil.constructToken(JsonUtil.buildJsonString('abc@example.com'), 'salt')
        Token token = new Token(constructedToken, 'salt', TokenPayload.newInstance('abc@example.com', ['testFirm' : ['AUTH_USER']]))

        tokenStoreService.tokenRepository = Mock(TokenRepository) {
            1 * findByTokenKey(*_) >> {String strToken ->
                return Optional.of(token)
            }
        }

        expect:
        tokenStoreService.getValidPayload(constructedToken).isPresent()
    }



}