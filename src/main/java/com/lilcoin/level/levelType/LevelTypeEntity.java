package com.lilcoin.level.levelType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "level_type")
public class LevelTypeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String levelTitle;

  private Double levelPrice;
}