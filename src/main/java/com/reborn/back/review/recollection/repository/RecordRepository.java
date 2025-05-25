package com.reborn.back.review.recollection.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface RecordRepository extends JpaRepository<Record, Integer>, JpaSpecificationExecutor<Record> {
    List<Record> findByPet(Pet pet, Pageable pageable);

    Optional<Record> findTopByPetAndCreatedAtBetweenOrderByCreatedAtDesc(Pet pet, LocalDateTime dayStart, LocalDateTime dayEnd);

}
