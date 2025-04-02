package com.reborn.back.pet.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {
    Slice<Pet> findByUser(User user, Pageable pageable);
    Optional<Pet> findById(Integer petId);
}
