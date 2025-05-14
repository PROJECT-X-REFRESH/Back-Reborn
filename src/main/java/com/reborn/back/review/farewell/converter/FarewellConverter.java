package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.review.farewell.dto.FarewellResponseDto;
import com.reborn.back.review.farewell.dto.RebirthResponseDto.ReviewRebirthDto;
import com.reborn.back.review.farewell.dto.RecognizeResponseDto.RecognizeResDto;
import com.reborn.back.review.farewell.dto.RememberResponseDto.SimpleRememberDto;
import com.reborn.back.review.farewell.dto.RevealResponseDto.SimpleRevealDto;

import java.util.List;
import java.util.stream.Collectors;

public class FarewellConverter {

    public static FarewellResponseDto toReviewResponse(Farewell farewell) {
        int completedRememberCount = farewell.getClearedThings().size() / 2;

        List<SimpleRememberDto> completedRemembers = farewell.getRememberList().stream()
                .limit(completedRememberCount)
                .map(remember -> SimpleRememberDto.builder()
                        .id(remember.getId())
                        .build())
                .collect(Collectors.toList());

        return FarewellResponseDto.builder()
                .recognize(farewell.getRecognize() != null ?
                        RecognizeResDto.builder()
                                .id(farewell.getRecognize().getId())
                                .score(farewell.getRecognize().getScore())
                                .build()
                        : null)

                .revealList(farewell.getRevealList().stream()
                        .filter(reveal -> reveal.getEmotion() != null)
                        .map(reveal -> SimpleRevealDto.builder()
                                .id(reveal.getId())
                                .build())
                        .collect(Collectors.toList()))

                .rememberList(completedRemembers)

                .rebirth(farewell.getRebirth() != null &&
                        farewell.getRebirth().getPetPost() != null ?
                        ReviewRebirthDto.builder()
                                .id(farewell.getRebirth().getId())
                                .petPost(farewell.getRebirth().getPetPost())
                                .build()
                        : null)
                .build();
    }
}
