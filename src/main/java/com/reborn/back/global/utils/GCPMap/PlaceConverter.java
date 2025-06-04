package com.reborn.back.global.utils.GCPMap;

import com.reborn.back.global.utils.DistanceUtils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlaceConverter {

    public static PlaceResponseDto toDto(GooglePlacesResponse.PlaceDto placeDto) {
        if (placeDto == null) {
            return null;
        }

        String name = (placeDto.getDisplayName() != null)
                ? placeDto.getDisplayName().getText()
                : null;

        return PlaceResponseDto.builder()
                .displayName(name)
                .formattedAddress(placeDto.getFormattedAddress())
                .nationalPhoneNumber(placeDto.getNationalPhoneNumber())
                .openNow(placeDto.getOpenNow())
                .build();
    }

    public static PlaceResponseDto toDto(
            GooglePlacesResponse.PlaceDto placeDto,
            double userLat, double userLng) {

        // 거리 계산 (Haversine)
        double distance = DistanceUtils.haversine(
                userLat, userLng,
                placeDto.getLocation().getLatitude(), placeDto.getLocation().getLongitude());

        double round = Math.round(distance * 100) / 100.0;   // 소수 두 자리

        return PlaceResponseDto.builder()
                .displayName(placeDto.getDisplayName().getText())
                .formattedAddress(placeDto.getFormattedAddress())
                .nationalPhoneNumber(placeDto.getNationalPhoneNumber())
                .distanceInMeters(round)
                .openNow(placeDto.getOpenNow())   // 중첩 객체에서 값 꺼냄
                .build();
    }
}