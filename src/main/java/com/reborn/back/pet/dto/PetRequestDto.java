package com.reborn.back.pet.dto;

import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDto {
    private String name;
    private PetType petCase;
    private LocalDate birth;
    private LocalDate death;
    private PetColor color;
}