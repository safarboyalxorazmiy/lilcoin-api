package com.lilcoin.userInvite;

import com.lilcoin.user.User;
import com.lilcoin.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInviteService {
  private final UserInviteRepository userInviteRepository;
  private final UserRepository userRepository;

  public void create(String ownerBotId, Integer friendId) {
    Optional<User> byBotUserId = userRepository.findByBotUserId(Long.valueOf(ownerBotId));
    if (byBotUserId.isEmpty()) {
      return;
    }

    UserInviteEntity userInviteEntity = new UserInviteEntity();
    userInviteEntity.setOwnerId(byBotUserId.get().getId());
    userInviteEntity.setFriendId(friendId);
    userInviteRepository.save(userInviteEntity);
  }

  public String getInviteLinkByUserId(Integer id) {
    Optional<User> byId = userRepository.findById(id);
    return byId.map(user -> "https://t.me/@Lilcoin1_bot?start" + user.getBotUserId()).orElse("https://t.me/@Lilcoin1_bot?start/");
  }
}