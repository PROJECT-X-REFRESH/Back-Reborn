package com.reborn.back.global.utils.hira;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class HiraEvaluationService {
    @Value("${hira.asm.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * ykiho에 해당하는 병원 평가 상세등급 XML을 그대로 문자열로 반환합니다.
     */
    public String getHospitalEvaluationXml(String ykiho) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://apis.data.go.kr/B551182/hospAsmInfoService1/getHospAsmInfo1")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 1)
                .queryParam("ykiho", ykiho)
                .queryParam("_type", "xml")   // XML 포맷 요청
                .build()
                .toUriString();

        return restTemplate
                .getForObject(url, String.class);
    }

    /** ykiho 로 평가등급만 추출해서 반환합니다. */
    public String getHospitalEvaluationGrade(String ykiho) {
        try {
            String xml = getHospitalEvaluationXml(ykiho);
            // 1) 파싱 준비
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(is);

            // 2) <item> 노드 찾기
            NodeList items = doc.getElementsByTagName("item");
            if (items.getLength() == 0) {
                return null;
            }

            // 3) 첫 번째 <item> 내 <asmGrd09> 노드 찾기
            Element item = (Element) items.item(0);
            NodeList grades = item.getElementsByTagName("asmGrd09");
            if (grades.getLength() == 0) {
                return null;
            }
            String grade = grades.item(0).getTextContent().trim();
            return grade.isEmpty() ? null : grade;

        } catch (Exception e) {
            log.error("HIRA 평가 XML 파싱 실패", e);
            throw new RuntimeException("병원 평가정보 조회 중 오류 발생");
        }
    }
}
