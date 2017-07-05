package au.com.bglcorp.config;

import au.com.bglcorp.service.authorization.AuthorizationService;
import au.com.bglcorp.service.authorization.AuthorizationServiceImpl;
import au.com.bglcorp.service.token.TokenStoreService;
import au.com.bglcorp.service.token.TokenStoreServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by senthurshanmugalingm on 4/07/2017.
 */
@Configuration
@Import({EmbeddedRedis.class, RedisConfig.class})
public class AppConfig {

    @Bean
    TokenStoreService tokenStoreService() {
        return new TokenStoreServiceImpl();
    }

    @Bean
    AuthorizationService authorizationService() {
        return new AuthorizationServiceImpl();
    }


}
