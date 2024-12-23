package com.lilcoin.userInvite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInviteRepository extends JpaRepository<UserInviteEntity, Long> {
  List<UserInviteEntity> findByOwnerId(Integer ownerId);

  List<UserInviteEntity> findByFriendId(Integer friendId);
}