package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.entity.Emotion;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Reveal;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.review.farewell.converter.RevealConverter;
import com.reborn.back.review.farewell.dto.RevealRequestDto.RevealReqDto;
import com.reborn.back.review.farewell.dto.RevealResponseDto;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import com.reborn.back.review.farewell.repository.RevealRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevealService {
    private final RevealRepository revealRepository;
    private final FarewellRepository farewellRepository;

    @Transactional
    public Reveal createReveal(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Reveal reveal = RevealConverter.saveReveal(farewell);
        revealRepository.save(reveal);

        return reveal;
    }

    @Transactional
    public void updateRevealActivity(Integer farewellId, String activityType) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Reveal reveal = revealRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> new GeneralException(ErrorCode.REVEAL_NOT_FOUND));

        // activityType에 따라 적절한 필드를 true로 변경
        switch (activityType.toLowerCase()) {
            case "feed":
                reveal.setFeed(true);
                break;
            case "snack":
                reveal.setSnack(true);
                break;
            case "walk":
                reveal.setWalk(true);
                break;
            default:
                throw new GeneralException(ErrorCode.INVALID_ACTIVITY_TYPE);
        }

        revealRepository.save(reveal);
    }

    @Transactional
    public Reveal writeReveal(Integer farewellId, RevealReqDto revealReqDto) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Reveal reveal = revealRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVEAL_NOT_FOUND));

        Emotion emotion = new Emotion(
                //revealReqDto.getPos(),
                //revealReqDto.getNeg(),
                revealReqDto.getEmotionState()
        );

        reveal.setContents(revealReqDto.getContents());
        reveal.setEmotion(emotion);

        farewell.setStep(farewell.getStep() + 1);

        return reveal;
    }

    public RevealResponseDto.DetailRevealDto getDetailReveal(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Reveal reveal = revealRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVEAL_NOT_FOUND));

        return RevealConverter.toDto(reveal);
    }

    public RevealResponseDto.ReviewRevealDto getReviewReveal(Integer revealId) {
        Reveal reveal = revealRepository.findById(revealId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REVEAL_NOT_FOUND));

        return RevealConverter.toReviewDto(reveal);
    }
}
