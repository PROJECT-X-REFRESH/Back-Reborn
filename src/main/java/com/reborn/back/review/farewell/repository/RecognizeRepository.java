package com.reborn.back.review.farewell.repository;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Recognize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecognizeRepository extends JpaRepository<Recognize, Long>, JpaSpecificationExecutor<Recognize> {

    Optional<Recognize> findByFarewell(Farewell farewell);

    Optional<Recognize> findById(Integer recognizeId);
}
