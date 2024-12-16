package com.lilcoin.exception;

public class DeviceNotAllowedException extends RuntimeException {
  public DeviceNotAllowedException(String message) {
    super(message);
  }
}