package com.reborn.back.global.utils.hira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HiraInfoService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${hira.info.service-key}")
    private String serviceKey;

    /*
     * 병원 기본목록을 XML 문자열로 반환
     *
     * @param pageNo    페이지 번호 (1 이상)
     * @param numOfRows 한 페이지 결과 수
     * @param yadmNm    (선택) 요양기관명 검색어
     */
    public String getPsychHospitalsJson(int pageNo, int numOfRows,
                                        double xPos, double yPos, double radius) {
        String uri = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList")
                .queryParam("ServiceKey", serviceKey)    // ← 대소문자 정확히!
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("clCd", 29)
                .queryParam("xPos", xPos)
                .queryParam("yPos", yPos)
                .queryParam("radius", radius)
                .queryParam("_type", "json")
                .build()
                .toUriString();

        return restTemplate.getForObject(uri, String.class);
    }
}
