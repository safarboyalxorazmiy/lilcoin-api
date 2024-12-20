package com.lilcoin.websocket;

import com.lilcoin.coin.coinDate.CoinDateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final CoinDateService coinDateService;

  public WebSocketConfig(CoinDateService coinDateService) {
    this.coinDateService = coinDateService;
  }

  @Bean
  public SimpleWebSocketHandler simpleWebSocketHandler() {
    return new SimpleWebSocketHandler(coinDateService);
  }


  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(simpleWebSocketHandler(), "/ws").setAllowedOrigins("*");
  }
}