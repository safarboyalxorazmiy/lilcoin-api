package com.lilcoin.level;

import com.lilcoin.coin.CoinEntity;
import com.lilcoin.coin.CoinRepository;
import com.lilcoin.level.levelType.LevelTypeEntity;
import com.lilcoin.level.levelType.LevelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelService {
  private final LevelRepository levelRepository;
  private final CoinRepository coinRepository;
  private final LevelTypeRepository levelTypeRepository;

  public Boolean upgrade(Integer userId) {
    Optional<LevelEntity> levelByUserId =
      levelRepository.findByUserId(userId);
    if (levelByUserId.isEmpty()) {
      LevelEntity level = new LevelEntity();
      level.setUserId(userId);
      level.setLevel(1);
      levelRepository.save(level);
      return true;
    }

    Optional<CoinEntity> coinUserId = coinRepository.findByUserId(userId);
    if (coinUserId.isEmpty()) {
      return false;
    }

    LevelEntity level = levelByUserId.get();
    CoinEntity coinEntity = coinUserId.get();

    Optional<LevelTypeEntity> byId =
      levelTypeRepository.findById(level.getLevel() + 1);
    if (byId.isEmpty()) {
      return false;
    }

    LevelTypeEntity levelType = byId.get();
    long subtractedCoin = coinEntity.getCoin() - levelType.getLevelPrice();
    if (subtractedCoin >= 0) {
      coinEntity.setCoin(subtractedCoin);
      coinRepository.save(coinEntity);

      level.setLevel(level.getLevel() + 1);
      levelRepository.save(level);
      return true;
    }

    return false;
  }

  public LevelInfoDTO getLevelInfo(Integer userId) {
    Optional<LevelEntity> levelByUserId = levelRepository.findByUserId(userId);
    if (levelByUserId.isEmpty()) {
      LevelEntity level = new LevelEntity();
      level.setUserId(userId);
      level.setLevel(1);
      levelRepository.save(level);

      LevelInfoDTO levelInfoDTO = new LevelInfoDTO();
      levelInfoDTO.setLevelTitle("Beginner");
      levelInfoDTO.setLevel(1);
      levelInfoDTO.setLevelPrice(60000L);
      return levelInfoDTO;
    }

    LevelEntity level = levelByUserId.get();
    Optional<LevelTypeEntity> levelTypeByUserId =
      levelTypeRepository.findById(level.getLevel());
    if (levelTypeByUserId.isEmpty()) {
      LevelInfoDTO levelInfoDTO = new LevelInfoDTO();
      levelInfoDTO.setLevelTitle("Beginner");
      levelInfoDTO.setLevel(1);
      levelInfoDTO.setLevelPrice(60000L);
      return levelInfoDTO;
    }

    LevelTypeEntity levelType = levelTypeByUserId.get();

    LevelInfoDTO levelInfoDTO = new LevelInfoDTO();
    levelInfoDTO.setLevelTitle(levelType.getLevelTitle());
    levelInfoDTO.setLevel(levelType.getId());
    levelInfoDTO.setLevelPrice(levelType.getLevelPrice());
    return levelInfoDTO;
  }
}