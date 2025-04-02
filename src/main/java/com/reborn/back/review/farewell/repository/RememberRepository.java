package com.reborn.back.review.farewell.repository;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Remember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RememberRepository extends JpaRepository<Remember, Long>, JpaSpecificationExecutor<Remember> {

    Optional<Remember> findTopByFarewellOrderByCreatedAtDesc(Farewell farewell);

    Optional<Remember> findById(Integer rememberId);
}
