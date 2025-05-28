package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Reveal;
import com.reborn.back.review.farewell.dto.RevealResponseDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RevealConverter {
    public static Reveal saveReveal(Farewell farewell) {
        return Reveal.builder()
                .feed(false)
                .walk(false)
                .snack(false)
                .farewell(farewell)
                .build();
    }

    public static RevealResponseDto.DetailRevealDto toDto(Reveal reveal) {
        boolean analyzed =
                reveal.getEmotion() != null &&
                        reveal.getEmotion().getState() != null;


        return RevealResponseDto.DetailRevealDto.builder()
                .feed(reveal.getFeed())
                .snack(reveal.getSnack())
                .walk(reveal.getWalk())
                .contents1(analyzed)
                .contents2(false)
                .build();
    }

    public static RevealResponseDto.ReviewRevealDto toReviewDto(Reveal reveal) {
        return RevealResponseDto.ReviewRevealDto.builder()
                .createdAt(reveal.getCreatedAt())
                .contents(reveal.getContents())
                .emotionState(reveal.getEmotion().getState())
                .build();
    }
}
