package com.reborn.back.review.farewell.repository;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Reveal;
import com.reborn.back.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevealRepository extends JpaRepository<Reveal, Long>, JpaSpecificationExecutor<Reveal> {

}
