package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Recognize;
import com.reborn.back.review.farewell.dto.RecognizeResponseDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecognizeConverter {
    public static Recognize saveRecognize(Farewell farewell) {
        return Recognize.builder()
                .feed(false)
                .walk(false)
                .snack(false)
                .score(null)
                .farewell(farewell)
                .build();
    }

    public static RecognizeResponseDto.DetailRecognizeDto toDto(Recognize recognize) {
        return RecognizeResponseDto.DetailRecognizeDto.builder()
                .feed(recognize.getFeed())
                .snack(recognize.getSnack())
                .walk(recognize.getWalk())
                .score(recognize.getScore())
                .build();
    }
}
