package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Rebirth;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.review.farewell.converter.RebirthConverter;
import com.reborn.back.review.farewell.dto.RebirthRequestDto.RebirthReqDto;
import com.reborn.back.review.farewell.dto.RebirthResponseDto.DetailRebirthDto;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import com.reborn.back.review.farewell.repository.RebirthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RebirthService {
    private final RebirthRepository rebirthRepository;
    private final FarewellRepository farewellRepository;

    @Transactional
    public Rebirth createRebirth(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Rebirth rebirth = RebirthConverter.saveRebirth(farewell);
        rebirthRepository.save(rebirth);

        return rebirth;
    }

    @Transactional
    public void updateRebirthActivity(Integer farewellId, String activityType) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Rebirth rebirth = rebirthRepository.findByFarewell(farewell)
                .orElseThrow(() -> new GeneralException(ErrorCode.REBIRTH_NOT_FOUND));

        // activityType에 따라 적절한 필드를 true로 변경
        switch (activityType.toLowerCase()) {
            case "wash":
                rebirth.setWash(true);
                break;
            case "dress":
                rebirth.setDress(true);
                break;
            case "yribbon":
                rebirth.setRibbon("YELLOW");
                break;
            case "bribbon":
                rebirth.setRibbon("BLACK");
                break;
            case "outro":
                rebirth.setOutro(true);
                farewell.setStep(farewell.getStep() + 1);
                break;
            default:
                throw new GeneralException(ErrorCode.INVALID_ACTIVITY_TYPE);
        }

        rebirthRepository.save(rebirth);
    }

    @Transactional
    public Rebirth writeRebirth(Integer farewellId, RebirthReqDto rebirthRequestDto) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Rebirth rebirth = rebirthRepository.findByFarewell(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REBIRTH_NOT_FOUND));

        rebirth.setPetPost(rebirthRequestDto.getPetPost());

        return rebirth;
    }

    public DetailRebirthDto getDetailRebirth(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Rebirth rebirth = rebirthRepository.findByFarewell(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REBIRTH_NOT_FOUND));

        return RebirthConverter.toDto(rebirth);
    }
}
