package com.lilcoin.websocket;

import com.lilcoin.coin.coinDate.CoinDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
public class SimpleWebSocketHandler extends TextWebSocketHandler {
  private final CoinDateService coinDateService;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println("New WebSocket connection: " + session.getId());
    session.sendMessage(new TextMessage("Welcome to the WebSocket server!"));
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage username) throws Exception {
    System.out.println("Received username: " + username.getPayload());
    coinDateService.increase(username.getPayload());
  }
}