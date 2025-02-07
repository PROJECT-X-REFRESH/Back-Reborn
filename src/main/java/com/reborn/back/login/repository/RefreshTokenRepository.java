package com.reborn.back.login.repository;

import com.reborn.back.login.auth.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken); //reissue

    boolean existsById(String username);

    void deleteById(String username);
}
