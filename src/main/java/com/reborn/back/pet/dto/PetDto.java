package com.reborn.back.pet.dto;

import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@NoArgsConstructor
public class PetDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class PetResponseDto {
        private Integer id;
        private String name;
        private PetType petCase;
        private LocalDate birth;
        private LocalDate death;
        private PetColor color;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PetRequestDto {
        private String name;
        private PetType petCase;
        private LocalDate birth;
        private LocalDate death;
        private PetColor color;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PetSimpleDto{
        private Integer id;
        private String name;
        private PetType petCase;
        private boolean death;
        private PetColor color;
    }
}
