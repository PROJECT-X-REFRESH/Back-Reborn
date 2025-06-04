package com.reborn.back.global.utils.GCPMap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "주변 상담소 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {

    @Schema(description = "상담소 이름")
    private String displayName;

    @Schema(description = "상담소 주소")
    private String formattedAddress;

    @Schema(description = "국가 전화번호 형식")
    private String nationalPhoneNumber;

    @Schema(description = "사용자 위치로부터 거리(미터)")
    private Double distanceInMeters;

    @Schema(description = "현재 영업 중인지 여부")
    private Boolean openNow;
}