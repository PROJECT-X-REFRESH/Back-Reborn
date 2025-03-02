package com.reborn.back.review.recollection.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RemindRepository extends JpaRepository<Remind, Long>, JpaSpecificationExecutor<Remind> {
    boolean existsByPetAndCreatedAtBetween(Pet pet, LocalDateTime start, LocalDateTime end);
}
