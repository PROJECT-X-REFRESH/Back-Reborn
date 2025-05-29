package com.reborn.back.review.recollection.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Recollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecollectionRepository extends JpaRepository<Recollection, Integer> {
    Optional<Recollection> findByPet(Pet pet);

    boolean existsByPet(Pet pet);
}