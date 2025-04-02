package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.review.farewell.converter.FarewellConverter;
import com.reborn.back.review.farewell.dto.FarewellResponseDto;
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

    @Transactional
    public FarewellResponseDto getFarewellReview(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        return FarewellConverter.toReviewResponse(farewell);
    }
}
