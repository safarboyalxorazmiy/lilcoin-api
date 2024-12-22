package com.lilcoin.websocket;

import com.lilcoin.coin.CoinService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final CoinService coinService;

  public WebSocketConfig(CoinService coinService) {
    this.coinService = coinService;
  }

  @Bean
  public SimpleWebSocketHandler simpleWebSocketHandler() {
    return new SimpleWebSocketHandler(coinService);
  }


  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(simpleWebSocketHandler(), "/ws").setAllowedOrigins("*");
  }
}