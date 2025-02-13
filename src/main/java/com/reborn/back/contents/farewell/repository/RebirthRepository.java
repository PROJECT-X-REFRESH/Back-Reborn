package com.reborn.back.contents.farewell.repository;

import com.reborn.back.domain.contents.farewell.Rebirth;
import com.reborn.back.domain.contents.farewell.Farewell;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RebirthRepository extends JpaRepository<Rebirth, Long>, JpaSpecificationExecutor<Rebirth> {
    Optional<Rebirth> findTopByPetOrderByDateDesc(Pet pet);

    Optional<Rebirth> findByPetAndDate(Pet pet, Integer date);

    Rebirth findByPetAndDateLessThan(Pet pet, Integer date);
}
