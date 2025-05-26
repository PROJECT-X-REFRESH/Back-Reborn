package com.reborn.back.review.farewell.dto;

import com.reborn.back.review.farewell.dto.RecognizeResponseDto.RecognizeResDto;
import com.reborn.back.review.farewell.dto.RememberResponseDto.SimpleRememberDto;
import com.reborn.back.review.farewell.dto.RevealResponseDto.SimpleRevealDto;
import com.reborn.back.review.farewell.dto.RebirthResponseDto.ReviewRebirthDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "작별 컨텐츠 리뷰 응답 DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarewellResponseDto {

    @Schema(description = "자가진단 결과")
    private RecognizeResDto recognize;

    @Schema(description = "감정 드러내기 ID 리스트")
    private List<SimpleRevealDto> revealList;

    @Schema(description = "추억 정리하기 ID 리스트")
    private List<SimpleRememberDto> rememberList;

    @Schema(description = "Rebirth 편지")
    private ReviewRebirthDto rebirth;
}
