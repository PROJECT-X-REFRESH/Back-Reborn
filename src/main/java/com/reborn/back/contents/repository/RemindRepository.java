package com.reborn.back.contents.repository;

import com.reborn.back.domain.contents.recollection.Remind;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RemindRepository extends JpaRepository<Remind, Long>, JpaSpecificationExecutor<Remind> {
    Optional<Remind> findTopByPetOrderByDateDesc(Pet pet);

    Optional<Remind> findByPetAndDate(Pet pet, Integer date);

    List<Remind> findAllByPetAndDateLessThanOrderByDateAsc(Pet pet, Integer date);

}