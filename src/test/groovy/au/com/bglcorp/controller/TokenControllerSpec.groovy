package au.com.bglcorp.controller

import au.com.bglcorp.dto.TokenDetails
import au.com.bglcorp.dto.UserDetails
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
    def "requestToken, should throw exception, when #message"() {
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

    def "requestToken, should return a valid token, when the User Details are valid"() {
        given :
        UserDetails userDetails = new UserDetails(username: 'abc@example.com', salt: 'salt', authorities: ['testFirm' : ['AUTH_USERS']])

        when :
        def response = mockMvc.perform(post("/token/requestToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(userDetails).toString())).andReturn()

        then :
        1 * tokenStoreService.createToken(*_) >> 'valid-token'
        response.response.contentAsString == 'valid-token'
    }

    @Unroll
    def "validateToken, should throw exception, "() {
        given :
        TokenDetails tokenDetails = new TokenDetails(token: token, firmShortName: firmShortName, authorities: authorities)

        when :
        def response = mockMvc.perform(post("/token/validateToken").contentType(MediaType.APPLICATION_JSON).content(new JsonBuilder(tokenDetails).toString())).andReturn()

        then :
        response.resolvedException.message == expectedResult

        where :
        token   | firmShortName | authorities | message                   | expectedResult
        null    | 'fimShrtNme'  | []          | "Token is null"           | 'Token cannot be null'
        'token' | null          | []          | "Firm Short Name is null" | 'Firm Short cannot Name be null'
        'token' | 'fimShrtNme'  | null        | "Authorities are null"    | 'Supported Authorities cannot be null'
    }
}