package com.reborn.back.login.repository;

import com.reborn.back.domain.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {

}
