package com.lilcoin.userInvite;

import com.lilcoin.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_invite_link")
public class UserInviteEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "owner_id")
  private Integer ownerId;

  @ManyToOne
  @JoinColumn(name = "owner_id", insertable = false, updatable = false)
  private User owner;

  @Column(name = "friend_id")
  private Integer friendId;

  @ManyToOne
  @JoinColumn(name = "friend_id", insertable = false, updatable = false)
  private User friend;
}