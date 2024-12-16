package com.lilcoin.coin;

import com.lilcoin.coin.coinDate.CoinDateService;
import com.lilcoin.exception.DeviceNotAllowedException;
import com.lilcoin.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua_parser.Parser;
import ua_parser.Client;

@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CoinController {
  private final CoinDateService coinDateService;

  @PostMapping("/increase")
  public ResponseEntity<Boolean> increase(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    if (userAgent == null || !userAgent.toLowerCase().contains("mobile") && !userAgent.toLowerCase().contains("tablet")) {
      System.out.println("Invalid device: " + userAgent);
      throw new DeviceNotAllowedException("Invalid device");
    }

    return ResponseEntity.ok(
      coinDateService.increase(getUser().getId())
    );
  }

  @GetMapping("/info")
  public ResponseEntity<Long> getInfo() {
    return ResponseEntity.ok(
      coinDateService.getInfo(getUser().getId())
    );
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
