package com.reborn.back.review.farewell.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "상담소 + HIRA 평가정보")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class CounselingCenterDto {

    @Schema(description = "상담소 이름")
    private String displayName;

    @Schema(description = "상담소 주소")
    private String formattedAddress;

    @Schema(description = "국가 전화번호")
    private String nationalPhoneNumber;

    @Schema(description = "위도")
    private Double latitude;

    @Schema(description = "경도")
    private Double longitude;

    @Schema(description = "ykiho (없으면 null)")
    private String ykiho;

    @Schema(description = "asmGrd09 (없으면 null)")
    private String grade;
}
