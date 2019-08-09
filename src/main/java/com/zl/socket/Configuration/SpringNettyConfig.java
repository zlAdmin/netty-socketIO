package com.zl.socket.Configuration;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;
import com.zl.socket.constant.WebSocketConstant;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * netty-socketio整合spring
 *
 * @author zhanglei
 * @ProjectName: netty-socketio
 * @create 2019-08-09 17:33
 * @Version: 1.0
 * <p>Copyright: Copyright (zl) 2019</p>
 **/
@Configuration
public class SpringNettyConfig implements CommandLineRunner {

    private SocketIOServer server;
    private PubSubStore pubSubStore;
    private SocketIONamespace chat1namespace;

    @Value("${websocket.server.host}")
    private String host;
    @Value("${websocket.server.port}")
    private Integer port;

    @Autowired
    private RedissonStoreFactory redissonStoreFactory;

    @Bean
    public SocketIOServer server()
    {
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setHostname(host);
        configuration.setPort(port);
        /** 客户端向服务器发送心跳时间间隔 */
        configuration.setPingInterval(25000);
        /** 时间内没有收到心跳消息触发超时时间 */
        configuration.setPingTimeout(60000);
        configuration.setBossThreads(2);
        configuration.setWorkerThreads(4);
        /** 协议升级超时时间 */
        configuration.setUpgradeTimeout(10000);

        configuration.setStoreFactory(redissonStoreFactory);

        configuration.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData data) {
                //TODO
                return true;
            }
        });
        server = new SocketIOServer(configuration);
        server.addNamespace(WebSocketConstant.Namespace);
        pubSubStore = server.getConfiguration().getStoreFactory().pubSubStore();
        return server;
    }

    @Bean
    public PubSubStore pubSubStore() {
        return pubSubStore;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }


    @Override
    public void run(String... args) throws Exception {
        server.start();
    }

    @PreDestroy
    public void destroy() {
        redissonStoreFactory.shutdown();
        server.stop();
    }
}
