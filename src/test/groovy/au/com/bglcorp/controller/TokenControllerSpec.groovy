package au.com.bglcorp.controller

import au.com.bglcorp.domain.token.TokenPayload
import au.com.bglcorp.dto.TokenDetails
import au.com.bglcorp.dto.UserDetails
import au.com.bglcorp.exception.InvalidTokenException
import au.com.bglcorp.service.authorization.AuthorizationService
import au.com.bglcorp.service.token.TokenStoreService
import groovy.json.JsonBuilder
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Unroll

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import spock.lang.Specification


/**
 * Created by senthurshanmugalingm on 11/07/2017.
 */
class TokenControllerSpec extends Specification {

    TokenController tokenController

    TokenStoreService tokenStoreService
    AuthorizationService authorizationService

    MockMvc mockMvc

    def setup() {
        tokenStoreService = Mock(TokenStoreService)
        authorizationService = Mock(AuthorizationService)

        tokenController = new TokenController()
        tokenController.tokenStoreService = tokenStoreService
        tokenController.authorizationService = authorizationService

        mockMvc = standaloneSetup(tokenController).build()
    }

    @Unroll
    def "requestToken, should return status code 404 and return '#expectedResult', when #message"() {
        given :
        UserDetails userDetails = new UserDetails(username: username, salt: salt, authorities: authorities)

        when :
        def response = mockMvc.perform(post("/token/requestToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(userDetails).toString())).andReturn()

        then :
        response.resolvedException.message == expectedResult

        where :
        username   | salt   | authorities | message                | expectedResult
        null       | 'salt' | [:]         | "Username is null"     | 'Username cannot be null'
        'username' | null   | [:]         | "Salt is null"         | 'User Salt cannot be null'
        'username' | 'salt' | null        | "Authorities are null" | 'Firm Authorities cannot be null'
    }

    def "requestToken, should return status code 200 and a valid token in the body, when the User Details are valid"() {
        given :
        UserDetails userDetails = new UserDetails(username: 'abc@example.com', salt: 'salt', authorities: ['testFirm' : ['AUTH_USERS']])

        when :
        def response = mockMvc.perform(post("/token/requestToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(userDetails).toString())).andReturn()

        then :
        1 * tokenStoreService.createToken(*_) >> 'valid-token'

        and :
        response.response.contentAsString == '{"result":"valid-token"}'
    }

    @Unroll
    def "validateToken, should return status code 404 and return '#expectedResult', when #message"() {
        given :
        TokenDetails tokenDetails = new TokenDetails(token: token, firmShortName: firmShortName, authorities: authorities)

        when :
        def response = mockMvc.perform(post("/token/validateToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(tokenDetails).toString())).andReturn()

        then :
        response.response.status == 404
        response.resolvedException.message == expectedResult

        where :
        token   | firmShortName | authorities | message                   | expectedResult
        null    | 'fimShrtNme'  | []          | "Token is null"           | 'Token cannot be null'
        'token' | null          | []          | "Firm Short Name is null" | 'Firm Short cannot Name be null'
        'token' | 'fimShrtNme'  | null        | "Authorities are null"    | 'Supported Authorities cannot be null'
    }

    def "validateToken, should return status code 404 and message 'Could not validate token', when the token cannot be validated"() {
        given :
        TokenDetails tokenDetails = new TokenDetails(token: 'tone', firmShortName: 'firm', authorities: ['AUTH_USERS_VIEW'])

        when :
        def response = mockMvc.perform(post("/token/validateToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(tokenDetails).toString())).andReturn()

        then :
        1 * tokenStoreService.getValidPayload(*_) >> Optional.ofNullable(null)

        and :
        response.resolvedException.message == 'Could not validate token'
    }

    def "validateToken, should return status 200 and {result:true}, when the Token is valid, and the User can access a given Authority"() {
        given :
        TokenDetails tokenDetails = new TokenDetails(token: 'tone', firmShortName: 'firm', authorities: ['AUTH_USERS_VIEW'])

        when :
        def response = mockMvc.perform(post("/token/validateToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(tokenDetails).toString())).andReturn()

        then :
        1 * tokenStoreService.getValidPayload(*_) >> Optional.of(TokenPayload.newInstance('abc@example.com', ['firm' : ['AUTH_USERS_VIEW']]))
        1 * authorizationService.isAuthorized(*_) >> true

        and :
        response.response.status == 200
        response.response.contentAsString == '{"result":true}'
    }

    def "validateToken, should return status 403 Forbidden, when the Token is valid, and the User cannot access a given Authority"() {
        given :
        TokenDetails tokenDetails = new TokenDetails(token: 'tone', firmShortName: 'firm', authorities: ['AUTH_USERS_VIEW'])

        when :
        def response = mockMvc.perform(post("/token/validateToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(tokenDetails).toString())).andReturn()

        then :
        1 * tokenStoreService.getValidPayload(*_) >> Optional.of(TokenPayload.newInstance('abc@example.com', ['firm' : ['AUTH_USERS_VIEW']]))
        1 * authorizationService.isAuthorized(*_) >> false

        and :
        response.response.status == 403
        response.resolvedException.message == 'No Authority found for this request'
    }

    def "removeToken, should return status 404, when the underlying token store cannot find the Token"() {
        when :
        def response = mockMvc.perform(delete("/token/removeToken/${UUID.randomUUID().toString()}").contentType(MediaType.APPLICATION_JSON)).andReturn()

        then :
        1 * tokenStoreService.deleteToken(*_) >> {throw new InvalidTokenException("Could not validate token")}

        and :
        response.response.status == 404
    }

    def "removeToken, should delete the token from the underlying token store"() {
        when :
        def response = mockMvc.perform(delete("/token/removeToken/${UUID.randomUUID().toString()}").contentType(MediaType.APPLICATION_JSON)).andReturn()

        then :
        1 * tokenStoreService.deleteToken(*_) >> true

        and :
        response.response.contentAsString == '{"result":true}'
    }
}