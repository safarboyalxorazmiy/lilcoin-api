package com.lilcoin.userLanguage;

import com.lilcoin.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/language")
@RequiredArgsConstructor
public class UserLanguageController {
  private final UserLanguageService userLanguageService;

  @PostMapping("/set/{lang}")
  public ResponseEntity<Void> setLanguage(@PathVariable LanguageType lang) {
    userLanguageService.setLanguage(lang, getUser().getId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/get")
  public ResponseEntity<LanguageType> getLang() {
    return ResponseEntity.ok(userLanguageService.getLanguageByUserId(getUser().getId()));
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}