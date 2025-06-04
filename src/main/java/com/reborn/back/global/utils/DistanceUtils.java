package com.reborn.back.global.utils;

public final class DistanceUtils {

    private static final double EARTH_RADIUS_M = 6_371_000;   // 지구 반지름(미터)

    private DistanceUtils() {}

    // Haversine 공식: 두 위·경도 사이 거리를 미터 단위로 반환
    public static double haversine(double lat1, double lng1,
                                   double lat2, double lng2) {

        double toRad = Math.PI / 180.0;

        double dLat = (lat2 - lat1) * toRad;
        double dLng = (lng2 - lng1) * toRad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1 * toRad) * Math.cos(lat2 * toRad)
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_M * c;
    }
}
