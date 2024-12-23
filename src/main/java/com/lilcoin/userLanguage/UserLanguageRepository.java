package com.lilcoin.userLanguage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguageEntity, Long> {
  Optional<UserLanguageEntity> findByUserId(Integer userId);
}