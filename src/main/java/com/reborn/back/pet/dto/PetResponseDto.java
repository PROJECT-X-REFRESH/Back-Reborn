package com.reborn.back.pet.dto;

import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.reborn.back.domain.pet.Pet;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDto {
    private Integer id;
    private String name;
    private PetType petCase;
    private LocalDate birth;
    private LocalDate death;
    private PetColor color;

    public static PetResponseDto fromEntity(Pet pet) {
        return PetResponseDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .petCase(pet.getPetCase())
                .birth(pet.getBirth())
                .death(pet.getDeath())
                .color(pet.getColor())
                .build();
    }
}
