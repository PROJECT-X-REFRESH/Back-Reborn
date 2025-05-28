package com.reborn.back.login.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "authCode로 JWT와 디바이스 토큰을 교환하기 위한 요청")
public class TokenExchangeRequest {

    @Schema(description = "딥링크로 받은 임시 인증 코드", example = "ABCD1234")
    private String code;

    @Schema(description = "클라이언트 디바이스의 푸시 토큰", example = "fCM-token-xyz")
    private String deviceToken;
}