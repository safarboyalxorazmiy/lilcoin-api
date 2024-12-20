package com.lilcoin.coin.coinDate;

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
public class CoinDateService {
  private final CoinDateRepository coinDateRepository;
  private final UserRepository userRepository;

  public Boolean increase(String username) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date currentDate = new Date();
    String formattedDate = formatter.format(currentDate);

    Optional<User> byEmail = userRepository.findByEmail(username + "@lilcoin1.ru");

    if (byEmail.isEmpty()) {
      return false;
    }

    List<CoinDateEntity> byDateAndUserId =
      coinDateRepository.findByDateAndUserId(formattedDate, byEmail.get().getId());
    if (byDateAndUserId.isEmpty()) {
      CoinDateEntity coinDateEntity = new CoinDateEntity();
      coinDateEntity.setCoin(1);
      coinDateEntity.setDate(formattedDate);
      coinDateEntity.setUserId(byEmail.get().getId());

      coinDateRepository.save(coinDateEntity);

      return true;
    }

    CoinDateEntity coinDateEntity = byDateAndUserId.get(0);
    if (coinDateEntity.getCoin() >= 30000) {
      return false;
    }

    coinDateEntity.setCoin(coinDateEntity.getCoin() + 1);
    coinDateRepository.save(coinDateEntity);
    return true;
  }

  public Long getInfo(Integer userId) {
    return coinDateRepository.sumCoinByUserId(userId);
  }
}