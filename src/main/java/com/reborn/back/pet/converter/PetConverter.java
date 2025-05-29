package com.reborn.back.pet.converter;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.user.User;
import com.reborn.back.pet.dto.PetDto;

public class PetConverter {
    public static PetDto.PetResponseDto fromEntity(Pet pet) {
        return PetDto.PetResponseDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .petCase(pet.getPetCase())
                .birth(pet.getBirth())
                .death(pet.getDeath())
                .color(pet.getColor())
                .build();
    }
    public static Pet toPetEntity(PetDto.PetRequestDto dto, User user) {
        Pet.PetBuilder builder = Pet.builder()
                .name(dto.getName())
                .petCase(dto.getPetCase())
                .birth(dto.getBirth())
                .color(dto.getColor())
                .user(user);

        if (dto.getDeath() != null) {
            builder.death(dto.getDeath());
        }
        return builder.build();
    }

    public static Farewell toNewFarewell(Pet pet) {
        return Farewell.builder()
                .pet(pet)
                .step(0)
                .build();
    }

    public static PetDto.PetSimpleDto toSimpleDto(Pet pet) {
        return PetDto.PetSimpleDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .petCase(pet.getPetCase())
                .death(pet.getDeath() != null)
                .color(pet.getColor())
                .build();
    }
}
