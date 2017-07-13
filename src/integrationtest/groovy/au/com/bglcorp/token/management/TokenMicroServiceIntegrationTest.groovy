package au.com.bglcorp.token.management

import au.com.bglcorp.BaseIntegrationTest
import au.com.bglcorp.dto.TokenDetails
import au.com.bglcorp.dto.UserDetails
import au.com.bglcorp.helper.RestHelper
import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest

/**
 * Created by senthurshanmugalingm on 12/07/2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenMicroServiceIntegrationTest extends BaseIntegrationTest {

    RESTClient restClient

    @LocalServerPort
    int port

    def setup() {
        restClient = new RESTClient("http://localhost:${port}")
    }


    def "calling /token/requestToken endpoint with valid User Details, should return result with a valid JWT Token"() {
        given :
        String path             = "/token/requestToken"
        UserDetails userDetails = new UserDetails(username: 'validuser@example.com',
                                                  salt: 'tasteSalty',
                                                  authorities: ['infierno' : ['AUTH_USERDETAILS']])

        when :
        def response = RestHelper.post("http://localhost:${port}", path, new JsonBuilder(userDetails).toString())

        then :
        println response
        response.result
    }

    def "calling /token/validateToken endpoint with valid Token details should return 'true', when the system can validate the token and the user can access the authorized resource"() {
        given :
        UserDetails userDetails = new UserDetails(username: 'validuser@example.com',
                salt: 'tasteSalty',
                authorities: ['infierno' : ['AUTH_USERDETAILS']])

        and :
        def response = RestHelper.post("http://localhost:${port}", "/token/requestToken", new JsonBuilder(userDetails).toString())

        expect :
        response.result

        and :
        TokenDetails tokenDetails = new TokenDetails(token: response.result, firmShortName: 'infierno', authorities: ['AUTH_USERDETAILS'])

        and :
        String path = "/token/validateToken"

        when :
        response = RestHelper.post("http://localhost:${port}", path, new JsonBuilder(tokenDetails).toString())

        then :
        response.result == true
    }

    def "calling /token/validateToken endpoint with valid Token details should return status 404 - Not found, when the system cannot validate the token"() {
        given :
        TokenDetails tokenDetails = new TokenDetails(token: UUID.randomUUID().toString(), firmShortName: 'infierno', authorities: ['AUTH_DELETEUSER'])

        and :
        String path = "/token/validateToken"

        when :
        def response = RestHelper.post("http://localhost:${port}", path, new JsonBuilder(tokenDetails).toString())

        then :
        response.status == 404
        response.message == 'Could not validate token'
    }

    def "calling /token/validateToken endpoint with valid Token details should return status 403 - Forbidden, when the system can validate the token but the user cannot access the authorized resource"() {
        given :
        UserDetails userDetails = new UserDetails(username: 'validuser@example.com',
                salt: 'tasteSalty',
                authorities: ['infierno' : ['AUTH_USERDETAILS']])

        and :
        def response = RestHelper.post("http://localhost:${port}", "/token/requestToken", new JsonBuilder(userDetails).toString())

        expect :
        response.result

        and :
        TokenDetails tokenDetails = new TokenDetails(token: response.result, firmShortName: 'infierno', authorities: ['AUTH_DELETEUSER'])

        and :
        String path = "/token/validateToken"

        when :
        response = RestHelper.post("http://localhost:${port}", path, new JsonBuilder(tokenDetails).toString())

        then :
        response.status == 403
        response.message == 'No Authority found for this request'
    }

    def "calling /token/validateToken endpoint with valid Token details should return status 403 - Forbidden, when the system can validate the token but the user cannot access a given firm"() {
        given :
        UserDetails userDetails = new UserDetails(username: 'validuser@example.com',
                salt: 'tasteSalty',
                authorities: ['infierno' : ['AUTH_USERDETAILS']])

        and :
        def response = RestHelper.post("http://localhost:${port}", "/token/requestToken", new JsonBuilder(userDetails).toString())

        expect :
        response.result

        and :
        TokenDetails tokenDetails = new TokenDetails(token: response.result, firmShortName: 'invalidFirm', authorities: ['AUTH_DELETEUSER'])

        and :
        String path = "/token/validateToken"

        when :
        response = RestHelper.post("http://localhost:${port}", path, new JsonBuilder(tokenDetails).toString())

        then :
        response.status == 403
        response.message == 'No Authority found for this request'
    }

    def "calling /token/removeToken, should return true, when the System can find the token and remove it from it's underlying data store"() {
        given :
        UserDetails userDetails = new UserDetails(username: 'validuser@example.com',
                salt: 'tasteSalty',
                authorities: ['infierno' : ['AUTH_USERDETAILS']])

        and :
        def response = RestHelper.post("http://localhost:${port}", "/token/requestToken", new JsonBuilder(userDetails).toString())

        expect :
        response.result

        and :
        String path = "/token/removeToken/${response.result}"

        when :
        response = RestHelper.delete("http://localhost:${port}", path)

        then :
        response.result == true
    }

    def "calling /token/removeToken, should return status 404 - Not Found, when the token to be removed cannot be found in the token store"() {
        given :
        String path = "/token/removeToken/${UUID.randomUUID().toString()}"

        when :
        def response = RestHelper.delete("http://localhost:${port}", path)

        then :
        response.status == 404
        response.message == 'Could not validate token'
    }

}