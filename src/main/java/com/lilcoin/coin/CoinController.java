package com.lilcoin.coin;

import com.lilcoin.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CoinController {
  private final CoinService coinDateService;

//  @PostMapping("/increase")
//  public ResponseEntity<Boolean> increase(HttpServletRequest request) {
//    String userAgent = request.getHeader("User-Agent");
//    if (userAgent == null || !userAgent.toLowerCase().contains("mobile") && !userAgent.toLowerCase().contains("tablet")) {
//      System.out.println("Invalid device: " + userAgent);
//      throw new DeviceNotAllowedException("Invalid device");
//    }
//
//    return ResponseEntity.ok(
//      coinDateService.increase(getUser().getId())
//    );
//  }

  @GetMapping("/info")
  public ResponseEntity<Double> getInfo() {
    Double coinInfo = coinDateService.getInfo(getUser().getId());

    if (coinInfo == null) {
      coinInfo = 0.0;
    }

    System.out.println("Coins: " + coinInfo);
    return ResponseEntity.ok(
      coinInfo
    );
  }

  @GetMapping("/info/by/current/date")
  public ResponseEntity<Double> getInfoByCurrentDate() {
    Double coinInfo = coinDateService.getInfoByCurrentDate(getUser().getId());

    if (coinInfo == null) {
      coinInfo = 0.0;
    }

    System.out.println("Coins: " + coinInfo);
    return ResponseEntity.ok(
      coinInfo
    );
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
