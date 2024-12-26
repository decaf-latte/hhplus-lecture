package com.hhplus.application.vo;

import com.hhplus.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserVO {

  private Long userId;
  private String name;

  @Builder
  public UserVO(Long userId, String name) {
    this.userId = userId;
    this.name = name;
  }

  public static UserVO from(User user) {
    return UserVO.builder().userId(user.getUserId()).name(user.getName()).build();
  }
}