package com.reborn.back.review.farewell.converter;

import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.review.farewell.Rebirth;
import com.reborn.back.review.farewell.dto.RebirthResponseDto.DetailRebirthDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RebirthConverter {
    public static Rebirth saveRebirth(Farewell farewell) {
        return Rebirth.builder()
                .wash(false)
                .dress(false)
                .ribbon(null)
                .farewell(farewell)
                .build();
    }

    public static DetailRebirthDto toDto(Rebirth rebirth) {
        return DetailRebirthDto.builder()
                .wash(rebirth.getWash())
                .dress(rebirth.getDress())
                .ribbon(rebirth.getRibbon())
                .build();
    }
}
