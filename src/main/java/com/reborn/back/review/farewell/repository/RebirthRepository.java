package com.reborn.back.review.farewell.repository;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Rebirth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RebirthRepository extends JpaRepository<Rebirth, Long>, JpaSpecificationExecutor<Rebirth> {

    Optional<Rebirth> findByFarewell(Farewell farewell);

    Optional<Rebirth> findById(Integer rebirthId);

    @Query("""
    select r
    from Rebirth r
    join fetch r.farewell f
    join fetch f.pet p
    where r.id = :rebirthId
""")
    Optional<Rebirth> findWithFarewellAndPet(@Param("rebirthId") Integer rebirthId);
}
