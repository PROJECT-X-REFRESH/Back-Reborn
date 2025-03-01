package com.reborn.back.pet.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.pet.dto.PetRequestDto;
import com.reborn.back.pet.dto.PetResponseDto;
import com.reborn.back.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    @Transactional
    public Pet createPetProfile(PetRequestDto petReqDto, User user) {
        Pet.PetBuilder petBuilder = Pet.builder()
                .name(petReqDto.getName())
                .petCase(petReqDto.getPetCase())
                .birth(petReqDto.getBirth())
                .color(petReqDto.getColor())
                .user(user);
        if (petReqDto.getDeath() != null) {
            petBuilder.death(petReqDto.getDeath());
        }
        Pet pet = petBuilder.build();
        if (petReqDto.getDeath() != null) {
            Farewell farewell = Farewell.builder()
                    .pet(pet)
                    .step(0)
                    .build();
            pet.setFarewell(farewell);
        }
        petRepository.save(pet);
        return petRepository.save(pet);
    }

    public List<PetResponseDto> getPetList(User user, int scrollPosition, int fetchSize) {
        PageRequest pageRequest = PageRequest.of(scrollPosition, fetchSize);
        Slice<Pet> petSlice = petRepository.findByUser(user, pageRequest);

        // Pet 엔티티를 PetResponseDto로 변환
        return petSlice.getContent().stream()
                .map(pet -> PetResponseDto.builder()
                        .name(pet.getName())
                        .petCase(pet.getPetCase())
                        .birth(pet.getBirth())
                        .death(pet.getDeath())
                        .color(pet.getColor())
                        .build())
                .toList();
    }

        /**
         * 반려동물 프로필 수정
         */
    public Pet updatePetProfile(Integer petId, PetRequestDto petRequestDto, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));

        if (!pet.getUser().getName().equals(username)) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        pet.setName(petRequestDto.getName());
        pet.setPetCase(petRequestDto.getPetCase());
        pet.setBirth(petRequestDto.getBirth());
        pet.setDeath(petRequestDto.getDeath());
        pet.setColor(petRequestDto.getColor());

        return petRepository.save(pet);
    }

    /**
     * 반려동물 삭제
     */
    public void deletePet(Integer petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));

        if (!pet.getUser().getName().equals(username)) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }

        petRepository.delete(pet);
    }

}
