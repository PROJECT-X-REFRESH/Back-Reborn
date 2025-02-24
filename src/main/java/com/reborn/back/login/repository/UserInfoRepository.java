package com.reborn.back.login.repository;

import com.reborn.back.domain.user.User;
import com.reborn.back.domain.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findByUser(User user);
}