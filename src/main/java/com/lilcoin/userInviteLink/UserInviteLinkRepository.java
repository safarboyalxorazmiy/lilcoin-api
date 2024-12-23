package com.lilcoin.userInviteLink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInviteLinkRepository extends JpaRepository<UserInviteLinkEntity, Long> {
  Optional<UserInviteLinkEntity> findByUserId(Integer userId);
}