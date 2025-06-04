package com.reborn.back.global.utils.GCPMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * Google Places Search API 응답 객체
 */
@Schema(description = "Google Places API 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GooglePlacesResponse {

    @Schema(description = "Place 정보 리스트")
    private List<PlaceDto> places;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Schema(description = "Google Place 객체")
    public static class PlaceDto {

        // ── 기본 필드 ───────────────────────────────────────────
        @Schema(description = "상담소 이름 정보")
        private DisplayName displayName;

        @Schema(description = "상담소 주소")
        private String formattedAddress;

        @Schema(description = "국가 전화번호")
        private String nationalPhoneNumber;

        @Schema(description = "위치 정보")
        private Location location;

        // ── 영업시간 정보 ──────────────────────────────────────
        @JsonProperty("currentOpeningHours")
        private RegularOpeningHours regularOpeningHours;

        public Boolean getOpenNow() {
            return regularOpeningHours != null ? regularOpeningHours.getOpenNow() : null;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "영업시간 정보")
        public static class RegularOpeningHours {
            private Boolean openNow;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "상담소 이름을 담은 객체")
        public static class DisplayName {
            private String text;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "위도, 경도 정보를 담은 객체")
        public static class Location {
            private double latitude;
            private double longitude;
        }
    }
}
