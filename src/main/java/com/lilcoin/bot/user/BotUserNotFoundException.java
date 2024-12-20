package com.lilcoin.bot.user;

public class BotUserNotFoundException extends RuntimeException {
  public BotUserNotFoundException(String message) {
    super(message);
  }
}
