package com.reborn.back.review.farewell.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class FarewellSimpleDto {
    private String  name;
    private PetType petCase;
    private boolean death;
    private PetColor color;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer farewellId;

    public static FarewellSimpleDto fromEntity(Pet pet) {
        return FarewellSimpleDto.builder()
                .name(pet.getName())
                .petCase(pet.getPetCase())
                .death(pet.getDeath() != null)
                .color(pet.getColor())
                .farewellId(pet.getFarewell() != null
                        ? pet.getFarewell().getId()
                        : null)
                .build();
    }
}
