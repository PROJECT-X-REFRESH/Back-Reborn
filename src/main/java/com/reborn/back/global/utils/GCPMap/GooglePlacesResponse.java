package com.reborn.back.global.utils.GCPMap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "주변 상담소 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GooglePlacesResponse {
    private List<PlaceDto> places;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "Google Place 객체")
    public static class PlaceDto {
        @Schema(description = "상담소 이름 정보")
        private DisplayName displayName;

        @Schema(description = "상담소 주소")
        private String formattedAddress;

        @Schema(description = "국가 전화번호")
        private String nationalPhoneNumber;

        @Schema(description = "위치 정보")
        private Location location;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "상담소 이름을 담은 객체")
        public static class DisplayName {
            @Schema(description = "상담소 이름")
            private String text;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Schema(description = "위도, 경도 정보를 담은 객체")
        public static class Location {
            @Schema(description = "위도")
            private double latitude;

            @Schema(description = "경도")
            private double longitude;
        }
    }
}
