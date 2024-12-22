package com.lilcoin.level;

import com.lilcoin.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/level")
@RequiredArgsConstructor
public class LevelController {
  private final LevelService levelService;

  @PostMapping("/upgrade")
  public ResponseEntity<Boolean> upgrade() {
    return ResponseEntity.ok(levelService.upgrade(getUser().getId()));
  }

  @GetMapping("/info")
  public ResponseEntity<LevelInfoDTO> getLevelInfo() {
    return ResponseEntity.ok(levelService.getLevelInfo(getUser().getId()));
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}