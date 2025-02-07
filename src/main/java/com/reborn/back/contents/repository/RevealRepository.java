package com.reborn.back.contents.repository;

import com.reborn.back.domain.contents.Reveal;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RevealRepository extends JpaRepository<Reveal, Long>, JpaSpecificationExecutor<Reveal> {
    Optional<Reveal> findTopByPetOrderByDateDesc(Pet pet);

    List<Reveal> findAllByDate(Integer date);

    Optional<Reveal> findByPetAndDate(Pet pet, Integer date);

    List<Reveal> findAllByPetAndDateLessThanOrderByDateAsc(Pet pet, Integer date);
}
