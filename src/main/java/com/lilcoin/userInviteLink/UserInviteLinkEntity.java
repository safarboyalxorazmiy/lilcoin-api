package com.lilcoin.userInviteLink;

import com.lilcoin.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_invite_link")
public class UserInviteLinkEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String inviteLink;

  @Column(name = "user_id")
  private Integer userId;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private User user;
}