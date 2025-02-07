package com.reborn.back.contents.repository;

import com.reborn.back.domain.contents.Reborn;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RebornRepository extends JpaRepository<Reborn, Long>, JpaSpecificationExecutor<Reborn> {
    Optional<Reborn> findTopByPetOrderByDateDesc(Pet pet);

    Optional<Reborn> findByPetAndDate(Pet pet, Integer date);

    Reborn findByPetAndDateLessThan(Pet pet, Integer date);
}
