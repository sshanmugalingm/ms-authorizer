package au.com.bglcorp.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by senthurshanmugalingm on 3/07/2017.
 */
@Configuration
@Profile("dev")
@PropertySource("classpath:/application.properties")
public class EmbeddedRedis {

    private static final Log logger = LogFactory.getLog(EmbeddedRedis.class);

    private RedisServer redisServer;

    @Value("${redis.server.port}")
    private int port;

    @PostConstruct
    public void startRedis() throws IOException, URISyntaxException {
        logger.info("Starting Embedded Redis Server.");

        redisServer = new RedisServer(port);
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() throws InterruptedException {
        if (redisServer != null) {
            logger.info("Stopping Embedded Redis Server");
            redisServer.stop();
        }
    }

}
