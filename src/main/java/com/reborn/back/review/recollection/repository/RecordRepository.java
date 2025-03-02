package com.reborn.back.review.recollection.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {
    boolean existsByPetAndCreatedAtBetween(Pet pet, LocalDateTime start, LocalDateTime end);
}
