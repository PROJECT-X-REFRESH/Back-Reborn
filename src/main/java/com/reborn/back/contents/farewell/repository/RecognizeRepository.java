package com.reborn.back.contents.farewell.repository;

import com.reborn.back.domain.contents.farewell.Recognize;
import com.reborn.back.domain.contents.farewell.Farewell;
import com.reborn.back.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecognizeRepository extends JpaRepository<Recognize, Long>, JpaSpecificationExecutor<Recognize> {

}