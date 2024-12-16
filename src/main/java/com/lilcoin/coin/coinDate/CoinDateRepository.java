package com.lilcoin.coin.coinDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoinDateRepository extends JpaRepository<CoinDateEntity, Long> {
  List<CoinDateEntity> findByDateAndUserId(String date, Integer userId);

  @Query("SELECT SUM(c.coin) FROM CoinDateEntity c WHERE c.userId = :userId")
  Long sumCoinByUserId(@Param("userId") Integer userId);

}