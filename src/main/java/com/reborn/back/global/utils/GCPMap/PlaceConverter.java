package com.reborn.back.global.utils.GCPMap;

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

        Double lat = (placeDto.getLocation() != null)
                ? placeDto.getLocation().getLatitude()
                : null;

        Double lng = (placeDto.getLocation() != null)
                ? placeDto.getLocation().getLongitude()
                : null;

        return PlaceResponseDto.builder()
                .displayName(name)
                .formattedAddress(placeDto.getFormattedAddress())
                .nationalPhoneNumber(placeDto.getNationalPhoneNumber())
                .latitude(lat)
                .longitude(lng)
                .build();
    }
}