package au.com.bglcorp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification


/**
 * Created by senthurshanmugalingm on 12/07/2017.
 */

@ContextConfiguration
@SpringBootTest
class BaseIntegrationTest extends Specification {

    @Autowired
    WebApplicationContext context

    def "should bootup without errors"() {
        expect:
        context
    }

}