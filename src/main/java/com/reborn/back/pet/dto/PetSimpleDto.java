package com.reborn.back.pet.dto;

import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import com.reborn.back.domain.pet.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetSimpleDto{
    private Integer id;
    private String name;
    private PetType petCase;
    private boolean death;
    private PetColor color;
    public static PetSimpleDto fromEntity(Pet pet) {
        return PetSimpleDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .petCase(pet.getPetCase())
                .death(pet.getDeath()!=null)
                .color(pet.getColor())
                .build();
    }

}