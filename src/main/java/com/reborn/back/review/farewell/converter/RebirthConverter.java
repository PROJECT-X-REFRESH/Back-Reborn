package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.entity.RebirthStep;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Rebirth;
import com.reborn.back.review.farewell.dto.RebirthResponseDto;
import com.reborn.back.review.farewell.dto.RebirthResponseDto.DetailRebirthDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RebirthConverter {
    public static Rebirth saveRebirth(Farewell farewell) {
        return Rebirth.builder()
                .wash(false)
                .clothes(false)
                .ribbon(null)
                .farewell(farewell)
                .build();
    }

    public static DetailRebirthDto toDto(Rebirth r) {
        return DetailRebirthDto.builder()
                .nextStep(calcNextStep(r))
                .build();
    }

    public static RebirthResponseDto.SimpleRebirthDto toReviewDto(Rebirth rebirth, String username, String petname) {
        return RebirthResponseDto.SimpleRebirthDto.builder()
                .petPost(rebirth.getPetPost())
                .username(username)
                .petName(petname)
                .build();
    }

    private static RebirthStep calcNextStep(Rebirth r) {
        if (!Boolean.TRUE.equals(r.getWash())) return RebirthStep.WASH;
        if (!Boolean.TRUE.equals(r.getClothes())) return RebirthStep.CLOTHES;
        if (r.getRibbon() == null) return RebirthStep.RIBBON;
        if (r.getPetPost() == null) return RebirthStep.POST;
        return RebirthStep.OUTRO;
    }
}
