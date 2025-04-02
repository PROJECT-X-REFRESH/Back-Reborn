package com.reborn.back.review.farewell.repository;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Rebirth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RebirthRepository extends JpaRepository<Rebirth, Long>, JpaSpecificationExecutor<Rebirth> {

    Optional<Rebirth> findTopByFarewellOrderByCreatedAtDesc(Farewell farewell);
}
