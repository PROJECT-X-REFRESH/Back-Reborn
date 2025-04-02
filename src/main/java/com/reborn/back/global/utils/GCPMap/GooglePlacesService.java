package com.reborn.back.global.utils.GCPMap;

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlacesService {

    @Value("${google.places.api.key}") private String apiKey;
    @Value("${google.places.api.base-url}") private String baseUrl;
    @Value("${google.places.api.end-point}") private String endPoint;

    // WebClient 인스턴스를 직접 생성합니다.
    private final WebClient webClient = WebClient.builder()
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public Mono<List<GooglePlacesResponse.PlaceDto>> getNearbyCounselingCenters(double lat, double lng) {

        Map<String, Object> body = Map.of(
                "textQuery", "상담소",
                "pageSize", 10,
                "locationBias", Map.of(
                        "circle", Map.of(
                                "center", Map.of(
                                        "latitude", lat,
                                        "longitude", lng
                                ),
                                "radius", 50000.0
                        )
                )
        );

        return webClient.post()
                .uri(baseUrl+endPoint)
                .header("X-Goog-Api-Key", apiKey)
                .header("X-Goog-FieldMask", "places.displayName,places.formattedAddress,places.location,places.nationalPhoneNumber")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GooglePlacesResponse.class)
                .map(GooglePlacesResponse::getPlaces);
    }
}