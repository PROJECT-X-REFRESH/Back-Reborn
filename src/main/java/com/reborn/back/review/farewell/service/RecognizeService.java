package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Recognize;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.GCPMap.GooglePlacesResponse;
import com.reborn.back.global.utils.GCPMap.GooglePlacesService;
import com.reborn.back.global.utils.GCPMap.PlaceConverter;
import com.reborn.back.global.utils.GCPMap.PlaceResponseDto;
import com.reborn.back.review.farewell.converter.RecognizeConverter;
import com.reborn.back.review.farewell.dto.RecognizeRequestDto.RecognizeReqDto;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import com.reborn.back.review.farewell.repository.RecognizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecognizeService {
    private final RecognizeRepository recognizeRepository;
    private final FarewellRepository farewellRepository;
    private final GooglePlacesService googlePlacesService;

    public void createRecognize(Integer farewellId, RecognizeReqDto recognizeDto) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Recognize recognize = RecognizeConverter.saveRecognize(recognizeDto, farewell);

        farewell.setStep(farewell.getStep() + 1);

        recognizeRepository.save(recognize);
    }

    public List<PlaceResponseDto> getNearbyCounselingCenters(double lat, double lng) {
        // GooglePlacesService로부터 외부 API 응답(PlaceDto 리스트)을 동기식으로 가져옴
        List<GooglePlacesResponse.PlaceDto> googlePlaceDtos =
                googlePlacesService.getNearbyCounselingCenters(lat, lng).block();

        if (googlePlaceDtos == null) {
            return Collections.emptyList();
        }

        // 각 Google PlaceDto를 우리 서비스의 PlaceResponseDto로 변환
        return googlePlaceDtos.stream()
                .map(PlaceConverter::toDto)
                .collect(Collectors.toList());
    }
}
