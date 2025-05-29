package com.reborn.back.review.farewell.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Recognize;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.hira.HiraEvaluationService;
import com.reborn.back.global.utils.hira.HiraInfoService;
import com.reborn.back.review.farewell.converter.RecognizeConverter;
import com.reborn.back.review.farewell.dto.CounselingCenterDto;
import com.reborn.back.review.farewell.dto.RecognizeRequestDto;
import com.reborn.back.review.farewell.dto.RecognizeResponseDto;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import com.reborn.back.review.farewell.repository.RecognizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecognizeService {
    private final RecognizeRepository recognizeRepository;
    private final FarewellRepository farewellRepository;
    private final HiraInfoService hiraInfoService;
    private final HiraEvaluationService hiraEvalService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public Recognize createRecognize(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Recognize recognize = RecognizeConverter.saveRecognize(farewell);
        recognizeRepository.save(recognize);

        return recognize;
    }

    @Transactional
    public void updateRecognizeActivity(Integer farewellId, String activityType) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Recognize recognize = recognizeRepository.findByFarewell(farewell)
                .orElseThrow(() -> new GeneralException(ErrorCode.RECOGNIZE_NOT_FOUND));

        // activityType에 따라 적절한 필드를 true로 변경
        switch (activityType.toLowerCase()) {
            case "feed":
                recognize.setFeed(true);
                break;
            case "snack":
                recognize.setSnack(true);
                break;
            case "walk":
                recognize.setWalk(true);
                break;
            default:
                throw new GeneralException(ErrorCode.INVALID_ACTIVITY_TYPE);
        }

        recognizeRepository.save(recognize);
    }

    @Transactional
    public void saveScore(Integer farewellId, RecognizeRequestDto.RecognizeReqDto recognizeDto) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Recognize recognize = recognizeRepository.findByFarewell(farewell)
                .orElseThrow(() -> new GeneralException(ErrorCode.RECOGNIZE_NOT_FOUND));

        recognize.setScore(recognizeDto.getScore());
    }

    public RecognizeResponseDto.DetailRecognizeDto getDetailRecognize(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Recognize recognize = recognizeRepository.findByFarewell(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.RECOGNIZE_NOT_FOUND));

        return RecognizeConverter.toDto(recognize);
    }

    /** 10 km 반경 “정신” 키워드 상위 3곳 + HIRA 평가정보까지 한 번에 반환 */
    public List<CounselingCenterDto> getCounselingCentersWithGrade(double lat, double lng) {
        // 1) JSON 응답 가져오기
        String json = hiraInfoService.getPsychHospitalsJson(
                1, 3, lng, lat, 10000.0
        );

        List<CounselingCenterDto> result = new ArrayList<>();
        try {
            JsonNode root  = objectMapper.readTree(json);
            JsonNode items = root.path("response")
                    .path("body")
                    .path("items")
                    .path("item");

            for (JsonNode item : items) {
                String name  = item.path("yadmNm").asText(null);
                String addr  = item.path("addr").asText(null);
                String phone = item.path("telno").asText(null);
                String ykiho = item.path("ykiho").asText(null);

                double latitude  = item.path("YPos").asDouble(0);
                double longitude = item.path("XPos").asDouble(0);

                // 3) 평가등급 조회
                String grade = (ykiho == null)
                        ? "병원 평가정보가 없습니다"
                        : hiraEvalService.getHospitalEvaluationGrade(ykiho);
                if (grade == null) grade = "병원 평가정보가 없습니다";

                // 4) DTO 빌드
                result.add(CounselingCenterDto.builder()
                        .displayName(name)
                        .formattedAddress(addr)
                        .nationalPhoneNumber(phone)
                        .latitude(latitude)
                        .longitude(longitude)
                        .ykiho(ykiho)
                        .grade(grade)
                        .build()
                );
            }
        } catch (Exception e) {
            log.error("HIRA JSON 파싱 실패", e);
        }

        return result;
    }

    public RecognizeResponseDto.SimpleRecognizeResDto getReviewRecognize(Integer recognizeId) {
        Recognize recognize = recognizeRepository.findById(recognizeId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.RECOGNIZE_NOT_FOUND));

        return RecognizeConverter.toReviewDto(recognize);
    }
}
