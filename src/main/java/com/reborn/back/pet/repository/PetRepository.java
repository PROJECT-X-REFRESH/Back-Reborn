package com.reborn.back.pet.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {

    List<Pet> findAllByUserOrderByCreatedAtAsc(User user);
}
