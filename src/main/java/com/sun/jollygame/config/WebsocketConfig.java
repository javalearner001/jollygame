package com.sun.jollygame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 1.首先要注入ServerEndpointExporter，
 * 这个bean会自动注册使用了@ServerEndpoint注解声明的WebsocketEndpoint
 *
 * @author sunkai
 * @since 2021/11/3 3:32 下午
 */
@Configuration
public class WebsocketConfig {

    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }
}
