package com.lilcoin.userInviteLink;

import com.lilcoin.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invite/link")
@RequiredArgsConstructor
public class UserInviteLinkController {
  private final UserInviteLinkService userInviteLinkService;

  @GetMapping("/get")
  public ResponseEntity<String> getInviteLink() {
    return ResponseEntity.ok(userInviteLinkService.getInviteLinkByUserId(getUser().getId()));
  }

  private User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
