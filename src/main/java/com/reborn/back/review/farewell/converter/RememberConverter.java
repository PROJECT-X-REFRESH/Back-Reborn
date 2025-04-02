package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.entity.OrganizeType;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Remember;
import com.reborn.back.review.farewell.dto.RememberResponseDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RememberConverter {
    public static Remember saveRemember(Farewell farewell) {
        return Remember.builder()
                .feed(false)
                .snack(false)
                .walk(false)
                .content(null)
                .url(null)
                .farewell(farewell)
                .build();
    }

    public static RememberResponseDto.DetailRememberDto toDto(Remember remember, List<OrganizeType> remainingThings) {
        return RememberResponseDto.DetailRememberDto.builder()
                .feed(remember.getFeed())
                .snack(remember.getSnack())
                .walk(remember.getWalk())
                .contents(remember.getContent())
                .remainingThings(remainingThings)
                .build();
    }

    public static RememberResponseDto.ReviewRememberDto toReviewDto(Remember remember) {
        return RememberResponseDto.ReviewRememberDto.builder()
                .createdAt(remember.getCreatedAt())
                .url(remember.getUrl())
                .contents(remember.getContent())
                .build();
    }
}
