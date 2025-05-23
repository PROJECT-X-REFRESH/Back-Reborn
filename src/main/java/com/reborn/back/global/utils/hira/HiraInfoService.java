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
     * 병원 기본목록을 XML 문자열로 반환
     * @param pageNo 페이지 번호 (1 이상)
     * @param numOfRows 한 페이지 결과 수
     * @param yadmNm (선택) 요양기관명 검색어
     */
    public String getHospBasisListXml(int pageNo, int numOfRows, String yadmNm) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551182/hospInfoServicev2/getHospBasisList")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1)
                .queryParam("_type", "xml");

        if (yadmNm != null && !yadmNm.isEmpty()) {
            builder.queryParam("yadmNm", yadmNm);
        }

        String uri = builder.build().toUriString();
        return restTemplate.getForObject(uri, String.class);
    }
}
