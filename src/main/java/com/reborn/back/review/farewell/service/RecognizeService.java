package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Recognize;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.GCPMap.GooglePlacesResponse;
import com.reborn.back.global.utils.GCPMap.GooglePlacesService;
import com.reborn.back.global.utils.GCPMap.PlaceConverter;
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

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
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
    private final HiraInfoService hiraInfoService;
    private final HiraEvaluationService hiraEvalService;

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

        // 1) Google Places (동기) 조회
        List<GooglePlacesResponse.PlaceDto> placeDtos =
                googlePlacesService.getNearbyCounselingCenters(lat, lng).blockOptional()
                        .orElse(Collections.emptyList());

        // 2) 각 장소마다 ykiho → asmGrd09 추출
        return placeDtos.stream()
                .map(PlaceConverter::toDto)             // Google → PlaceResponseDto
                .map(place -> {                         // PlaceResponseDto → CounselingCenterDto
                    String ykiho = extractYkiho(place.getDisplayName());
                    String grade = (ykiho == null)
                            ? "병원 평가정보가 없습니다"
                            : extractGrade(ykiho);

                    return CounselingCenterDto.builder()
                            .displayName(place.getDisplayName())
                            .formattedAddress(place.getFormattedAddress())
                            .nationalPhoneNumber(place.getNationalPhoneNumber())
                            .latitude(place.getLatitude())
                            .longitude(place.getLongitude())
                            .ykiho(ykiho)
                            .grade(grade == null ? "병원 평가정보가 없습니다" : grade)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /** 병원명으로 ykiho 추출 (없으면 null) */
    private String extractYkiho(String hospName) {
        String xml = hiraInfoService.getHospBasisListXml(1, 1, hospName);
        try {
            var doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(
                            xml.getBytes(StandardCharsets.UTF_8)));
            var ykihoTags = doc.getElementsByTagName("ykiho");
            if (ykihoTags.getLength() == 0) return null;
            String ykiho = ykihoTags.item(0).getTextContent().trim();
            return ykiho.isBlank() ? null : ykiho;
        } catch (Exception e) {
            log.error("ykiho 파싱 실패 : {}", hospName, e);
            return null;
        }
    }

    /** ykiho 로 asmGrd09 추출 (없으면 null) */
    private String extractGrade(String ykiho) {
        return hiraEvalService.getHospitalEvaluationGrade(ykiho);
    }
}
