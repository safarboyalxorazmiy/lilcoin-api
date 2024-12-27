package com.lilcoin.coin;

import com.lilcoin.coin.coinDate.CoinDateEntity;
import com.lilcoin.coin.coinDate.CoinDateRepository;
import com.lilcoin.level.LevelEntity;
import com.lilcoin.level.LevelRepository;
import com.lilcoin.user.User;
import com.lilcoin.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoinService {
  private final CoinDateRepository coinDateRepository;
  private final CoinRepository coinRepository;
  private final UserRepository userRepository;
  private final LevelRepository levelRepository;

  public Boolean increase(String username) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date currentDate = new Date();
    String formattedDate = formatter.format(currentDate);

    Optional<User> byEmail = userRepository.findByEmail(username + "@lilcoin1.ru");

    if (byEmail.isEmpty()) {
      return false;
    }

    List<CoinDateEntity> coinDateByDateAndUserId =
      coinDateRepository.findByDateAndUserId(formattedDate, byEmail.get().getId());
    Optional<CoinEntity> coinByUserId = coinRepository.findByUserId(byEmail.get().getId());

    Optional<LevelEntity> levelByUserId = levelRepository.findByUserId(byEmail.get().getId());
    long coin = 1L;
    if (levelByUserId.isPresent()) {
      LevelEntity level = levelByUserId.get();
      coin = level.getLevel();
    }

    if (coinDateByDateAndUserId.isEmpty() || coinByUserId.isEmpty()) {
      CoinDateEntity coinDateEntity = new CoinDateEntity();
      coinDateEntity.setCoin(coin);
      coinDateEntity.setDate(formattedDate);
      coinDateEntity.setUserId(byEmail.get().getId());

      coinDateRepository.save(coinDateEntity);

      CoinEntity coinEntity = new CoinEntity();
      coinEntity.setCoin(coin);
      coinEntity.setUserId(byEmail.get().getId());
      coinRepository.save(coinEntity);
      return true;
    }

    CoinDateEntity coinDateEntity = coinDateByDateAndUserId.get(0);
    CoinEntity coinEntity = coinByUserId.get();

    if (coinDateEntity.getCoin() >= 30000) {
      return false;
    }

    coinDateEntity.setCoin(coinDateEntity.getCoin() + coin);
    coinEntity.setCoin(coinEntity.getCoin() + coin);
    coinDateRepository.save(coinDateEntity);
    coinRepository.save(coinEntity);
    return true;
  }

  public Long getInfo(Integer userId) {
    Optional<CoinEntity> byUserId = coinRepository.findByUserId(userId);
    if (byUserId.isEmpty()) {
      return 0L;
    }

    return byUserId.get().getCoin();
  }
}