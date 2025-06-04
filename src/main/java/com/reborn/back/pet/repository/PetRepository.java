package com.reborn.back.pet.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer>, JpaSpecificationExecutor<Pet> {
    Slice<Pet> findByUser(User user, Pageable pageable);
}
