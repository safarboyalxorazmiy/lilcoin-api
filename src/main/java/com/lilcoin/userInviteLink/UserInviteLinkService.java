package com.lilcoin.userInviteLink;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInviteLinkService {
  private final UserInviteLinkRepository userInviteLinkRepository;

  public String getInviteLinkByUserId(Integer userId) {
    Optional<UserInviteLinkEntity> byUserId =
      userInviteLinkRepository.findByUserId(userId);
    if (byUserId.isEmpty()) {
      return "";
    }

    return byUserId.get().getInviteLink();
  }
}