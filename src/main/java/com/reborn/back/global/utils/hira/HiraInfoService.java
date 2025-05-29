package com.reborn.back.global.utils.hira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HiraInfoService {
    @Value("${hira.info.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 정신병원 클러스터 검색용
     */
    public String getPsychHospitalsJson(int pageNo, int numOfRows,
                                        double xPos, double yPos, double radius) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList")
                .queryParam("serviceKey", serviceKey)    // 주의: serviceKey
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("clCd", 29)                  // 정신병원
                .queryParam("xPos", xPos)
                .queryParam("yPos", yPos)
                .queryParam("radius", radius)
                .queryParam("_type", "json")             // ← JSON 타입으로 변경
                .build()
                .toUriString();

        return restTemplate.getForObject(uri, String.class);
    }
}
