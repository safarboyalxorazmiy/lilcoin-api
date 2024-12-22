package com.lilcoin.coin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<CoinEntity, Long> {
  Optional<CoinEntity> findByUserId(Integer userId);
}