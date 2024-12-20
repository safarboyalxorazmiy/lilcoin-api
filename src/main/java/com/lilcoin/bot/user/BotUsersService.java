package com.lilcoin.bot.user;

import com.lilcoin.user.Role;
import com.lilcoin.user.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BotUsersService {
    private final BotUserRepository userRepository;

    public BotUsersService(BotUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BotUserEntity createUser(Long chatId, String firstName, String lastName) {
        Optional<BotUserEntity> byId = userRepository.findById(chatId);
        if (byId.isEmpty()) {
            BotUserEntity entity = new BotUserEntity();
            entity.setChatId(chatId);
            entity.setFirstName(firstName);
            entity.setLastName(lastName);
            entity.setRole(BotRole.ROLE_USER);
            entity.setRegisterAt(LocalDateTime.now());

            userRepository.save(entity);
            return entity;
        }

        return byId.get();
    }

    public Iterable<BotUserEntity> getAll() {
        return userRepository.findAll();
    }

    public BotRole getRoleByChatId(Long chatId) {
        Optional<BotUserEntity> userByChatId = userRepository.getUserByChatId(chatId);
        if (userByChatId.isEmpty()) {
            return null;
        }
        BotUserEntity entity = userByChatId.get();
        return entity.getRole();
    }

    public List<Long> getChatIdByRole(com.lilcoin.user.Role role) {
        List<BotUserEntity> usersByRole = userRepository.findByRole(role);
        List<Long> result = new ArrayList<>();
        for (BotUserEntity entity : usersByRole) {
            result.add(entity.getChatId());
        }

        return result;
    }

    public Boolean changeRole(Long chatId, BotRole role) {
        Optional<BotUserEntity> userByChatId = userRepository.getUserByChatId(chatId);
        if (userByChatId.isEmpty()) {
            throw new BotUserNotFoundException("There is no user by this id");
        }
        BotUserEntity entity = userByChatId.get();
        entity.setRole(role);
        userRepository.save(entity);
        return true;
    }

}
