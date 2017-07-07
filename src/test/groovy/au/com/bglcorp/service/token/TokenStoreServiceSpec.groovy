package au.com.bglcorp.service.token

import spock.lang.Specification


/**
 * Created by senthurshanmugalingm on 7/07/2017.
 */
class TokenStoreServiceSpec extends Specification {

    TokenStoreService tokenStoreService

    def setup() {
        tokenStoreService = new TokenStoreServiceImpl()
    }

}