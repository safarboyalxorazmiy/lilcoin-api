package com.lilcoin.userLanguage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLanguageService {
  private final UserLanguageRepository userLanguageRepository;

  public void setLanguage(LanguageType languageType, Integer userId) {
    Optional<UserLanguageEntity> byUserId = userLanguageRepository.findByUserId(userId);
    if (byUserId.isEmpty()) {
      UserLanguageEntity userLanguageEntity = new UserLanguageEntity();
      userLanguageEntity.setLanguageType(languageType);
      userLanguageEntity.setUserId(userId);
      userLanguageRepository.save(userLanguageEntity);
      return;
    }

    UserLanguageEntity userLanguageEntity = byUserId.get();
    userLanguageEntity.setLanguageType(languageType);
    userLanguageRepository.save(userLanguageEntity);
    return;
  }


  public LanguageType getLanguageByUserId(Integer userId) {
    Optional<UserLanguageEntity> byUserId = userLanguageRepository.findByUserId(userId);
    if (byUserId.isEmpty()) {
      return LanguageType.ENGLISH;
    }

    return byUserId.get().getLanguageType();
  }

}