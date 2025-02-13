package com.reborn.back.contents.recollection.repository;

import com.reborn.back.domain.contents.farewell.recollection.Record;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {
    Optional<Record> findTopByPetOrderByDateDesc(Pet pet);

    Optional<Record> findByPetAndDate(Pet pet, Integer date);

    Record findByPetAndDateLessThan(Pet pet, Integer date);
}
