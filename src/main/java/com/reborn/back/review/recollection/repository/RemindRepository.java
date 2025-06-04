package com.reborn.back.review.recollection.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Remind;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RemindRepository extends JpaRepository<Remind, Integer>, JpaSpecificationExecutor<Remind> {
    boolean existsByPetAndCreatedAtBetween(Pet pet, LocalDateTime start, LocalDateTime end);

    List<Remind> findByPet(Pet pet, Pageable pageable);

    Optional<Remind> findTopByPetAndCreatedAtBetweenOrderByCreatedAtDesc(Pet pet, LocalDateTime start, LocalDateTime end);

}
