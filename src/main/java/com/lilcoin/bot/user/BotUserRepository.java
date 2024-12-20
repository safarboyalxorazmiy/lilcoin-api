package com.lilcoin.bot.user;

import com.lilcoin.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotUserRepository extends CrudRepository<BotUserEntity, Long> {
  Optional<BotUserEntity> getUserByChatId(Long chatId);

  List<BotUserEntity> findByRole(Role role);
}
