package com.reborn.back.review.farewell.service;

import com.reborn.back.domain.entity.OrganizeType;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Remember;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.S3.AmazonS3Manager;
import com.reborn.back.review.farewell.converter.RememberConverter;
import com.reborn.back.review.farewell.dto.RememberRequestDto.RememberReqDto;
import com.reborn.back.review.farewell.dto.RememberResponseDto;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import com.reborn.back.review.farewell.repository.RememberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RememberService {
    private final RememberRepository rememberRepository;
    private final FarewellRepository farewellRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Transactional
    public Remember createRemember(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Remember remember = RememberConverter.saveRemember(farewell);
        rememberRepository.save(remember);

        return remember;
    }

    @Transactional
    public void updateRememberActivity(Integer farewellId, String activityType) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        Remember remember = rememberRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> new GeneralException(ErrorCode.REMEMBER_NOT_FOUND));

        // activityType에 따라 적절한 필드를 true로 변경
        switch (activityType.toLowerCase()) {
            case "feed":
                remember.setFeed(true);
                break;
            case "snack":
                remember.setSnack(true);
                break;
            case "walk":
                remember.setWalk(true);
                break;
            default:
                throw new GeneralException(ErrorCode.INVALID_ACTIVITY_TYPE);
        }

        rememberRepository.save(remember);
    }

    @Transactional
    public void writeRemember(Integer farewellId, RememberReqDto simpleRememberReqDto, String dirName, MultipartFile file) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Remember remember = rememberRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        String uploadFileUrl = null;

        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (ObjectUtils.isEmpty(contentType)) { // 확장자명이 존재하지 않을 경우 취소 처리
                throw GeneralException.of(ErrorCode.INVALID_FILE_CONTENT_TYPE_REMEMBER);
            }

            java.io.File uploadFile;
            try {
                uploadFile = amazonS3Manager.convert(file)
                        .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
            } catch (IOException e) {
                throw GeneralException.of(ErrorCode.FILE_CONVERT_FAIL_REMEMBER);
            }

            String fileName = dirName + amazonS3Manager.generateFileName(file);
            uploadFileUrl = amazonS3Manager.putS3(uploadFile, fileName);
        }

        remember.setContent(simpleRememberReqDto.getContents());
        remember.setUrl(uploadFileUrl);

        rememberRepository.save(remember);
    }

    @Transactional
    public void cleanThing(Integer farewellId, String cleanType) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> new GeneralException(ErrorCode.FAREWELL_NOT_FOUND));

        OrganizeType type;
        try {
            type = OrganizeType.valueOf(cleanType.toUpperCase());   // SNACK‧TOY‧BATH‧LIVING
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorCode.INVALID_ACTIVITY_TYPE);
        }

        Remember remember = rememberRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> new GeneralException(ErrorCode.REMEMBER_NOT_FOUND));

        if (remember.getCleanedThings().contains(type)) {
            return;
        }

        if (farewell.getClearedThings().contains(type)) {
            return;
        }

        remember.getCleanedThings().add(type);
        farewell.getClearedThings().add(type);
    }


    public RememberResponseDto.DetailRememberDto getDetailRemember(Integer farewellId) {
        Farewell farewell = farewellRepository.findById(farewellId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.FAREWELL_NOT_FOUND));

        Remember remember = rememberRepository.findTopByFarewellOrderByCreatedAtDesc(farewell)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REMEMBER_NOT_FOUND));

        Set<OrganizeType> done = farewell.getClearedThings();

        List<OrganizeType> remaining = Arrays.stream(OrganizeType.values())
                .filter(t -> !done.contains(t))
                .collect(Collectors.toList());

        return RememberConverter.toDto(remember, remaining);
    }

    public RememberResponseDto.ReviewRememberDto getReviewRemember(Integer rememberId) {
        Remember remember = rememberRepository.findById(rememberId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.REMEMBER_NOT_FOUND));

        return RememberConverter.toReviewDto(remember);
    }
}
