package com.lilcoin.coin.coinDate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinDateService {
  private final CoinDateRepository coinDateRepository;

  public Boolean increase(Integer userId) {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    Date currentDate = new Date();
    String formattedDate = formatter.format(currentDate);

    List<CoinDateEntity> byDateAndUserId = coinDateRepository.findByDateAndUserId(formattedDate, userId);
    if (byDateAndUserId.isEmpty()) {
      CoinDateEntity coinDateEntity = new CoinDateEntity();
      coinDateEntity.setCoin(1);
      coinDateEntity.setDate(formattedDate);
      coinDateEntity.setUserId(userId);

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