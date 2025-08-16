package com.sinse.chatappdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

// spring boot(내장 톰캣)에서 JSR-365(@ServerEndpoint)를 쓰려면 엔드포인트 스캔 및 등록 필요
// 등록용 설정 Exporter를 넣어줘야 탐지된다
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
