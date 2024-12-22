package com.lilcoin.bot.user;

import com.lilcoin.user.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor

@Entity
@Table(name = "bot_user")
public class BotUserEntity {
  @Id
  private Long chatId;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private LocalDateTime registerAt;

  @Enumerated(value = EnumType.STRING)
  @Column
  private BotRole role;
}
