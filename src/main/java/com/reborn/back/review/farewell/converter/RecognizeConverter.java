package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Recognize;
import com.reborn.back.review.farewell.dto.RecognizeRequestDto.RecognizeReqDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RecognizeConverter {
    public static Recognize saveRecognize(RecognizeReqDto recognize, Farewell farewell) {
        return Recognize.builder()
                .score(recognize.getScore())
                .farewell(farewell)
                .build();
    }
}
