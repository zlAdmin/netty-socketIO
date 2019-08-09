package com.zl.socket.Configuration;

import com.corundumstudio.socketio.store.RedissonStoreFactory;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redission
 *
 * @author zhanglei
 * @ProjectName: netty-socketio
 * @create 2019-08-09 17:25
 * @Version: 1.0
 * <p>Copyright: Copyright (acmtc) 2019</p>
 **/
@Configuration
public class RedisClientConfig {

    @Value("${websocket.redis.host}")
    private String host;

    @Value("${websocket.redis.port}")
    private String port;

    @Value("${websocket.redis.password}")
    private String pass;

    public interface REDISKEY{
        String IM_USERID = "im:%1s";
    }

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port).setPassword(pass);
        return Redisson.create(config);
    }

    @Bean
    RedissonStoreFactory redissonStoreFactory(RedissonClient redissonClient) {
        return new RedissonStoreFactory(redissonClient);
    }
}
