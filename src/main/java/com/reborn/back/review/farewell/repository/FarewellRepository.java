package com.reborn.back.review.farewell.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface FarewellRepository extends JpaRepository<Farewell, Long>, JpaSpecificationExecutor<Farewell> {
    Optional<Farewell> findById(Integer fId);

    void deleteAllByPetIn(Collection<Pet> pets);
}
