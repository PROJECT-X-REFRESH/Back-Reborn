package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.farewell.converter.FarewellConverter;
import com.reborn.back.review.farewell.dto.FarewellResponseDto;
import com.reborn.back.review.farewell.dto.FarewellSimpleDto;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FarewellService {
    private final FarewellRepository farewellRepository;
    private final PetRepository petRepository;

    public FarewellSimpleDto getPetSimpleDto(Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));

        return FarewellSimpleDto.fromEntity(pet);
    }

    @Transactional
    public FarewellResponseDto getFarewellReview(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        return FarewellConverter.toReviewResponse(farewell);
    }

    @Transactional
    public void increaseFstep(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        farewell.setStep(farewell.getStep()+1);
    }
}
