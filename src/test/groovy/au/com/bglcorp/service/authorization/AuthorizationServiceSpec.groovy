package au.com.bglcorp.service.authorization

import au.com.bglcorp.domain.token.TokenPayload
import au.com.bglcorp.dto.TokenDetails
import spock.lang.Specification
import spock.lang.Unroll


/**
 * Created by senthurshanmugalingm on 7/07/2017.
 */
class AuthorizationServiceSpec extends Specification {

    AuthorizationService authorizationService

    def setup() {
        authorizationService = new AuthorizationServiceImpl()
    }

    @Unroll
    def "isAuthorized, should return #expectedResult, when the token payload #mesage"() {
        given:
        TokenPayload tokenPayload = TokenPayload.newInstance("abc@example.com", ["validFirm": ["AUTH_VALID"]])
        TokenDetails tokenDetails = new TokenDetails(firmShortName: validatableFirmName, token: UUID.randomUUID().toString(), authorities: [validatableAuthority])

        expect:
        authorizationService.isAuthorized(tokenPayload, tokenDetails) == expectedResult

        where:
        expectedResult | validatableFirmName | validatableAuthority | message
        true           | "validFirm"         | "AUTH_VALID"         | "contains the firm and authority provided in the token details"
        false          | "invalidFirm"       | "AUTH_VALID"         | "does not contains the firm and authority provided in the token details"
        false          | "invalidFirm"       | "AUTH_INVALID"       | "does not contains the firm and does not contain the authority provided in the token details"
        false          | "validFirm"         | "AUTH_INVALID"       | "contains the firm and does not contain the authority provided in the token details"
    }
}