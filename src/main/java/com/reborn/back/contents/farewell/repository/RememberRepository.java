package com.reborn.back.contents.farewell.repository;

import com.reborn.back.domain.contents.farewell.Remember;
import com.reborn.back.domain.contents.farewell.Farewell;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RememberRepository extends JpaRepository<Remember, Long>, JpaSpecificationExecutor<Remember> {
    Optional<Remember> findTopByPetOrderByDateDesc(Pet pet);

    Optional<Remember> findByPetAndDate(Pet pet, Integer date);

    List<Remember> findAllByPetAndDateLessThanOrderByDateAsc(Pet pet, Integer date);

}
