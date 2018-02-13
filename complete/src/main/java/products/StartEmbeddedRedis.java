package products;

import org.springframework.stereotype.Controller;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Controller
public class StartEmbeddedRedis {
    private RedisServer redisServer;


    @PostConstruct
    private void startReddis() throws IOException {
        redisServer = new RedisServer(6379);
        redisServer.start();

    }

    @PreDestroy
    public void stopReddis(){
        redisServer.stop();
    }
}
