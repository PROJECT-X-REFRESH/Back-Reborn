package com.reborn.back.pet.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.pet.converter.PetConverter;
import com.reborn.back.pet.dto.PetDto;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.farewell.repository.FarewellRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final FarewellRepository farewellRepository;

    @Transactional
    public Pet findById(Integer pId) {
        return petRepository.findById(pId)
                .orElseThrow(() -> GeneralException.of(ErrorCode.PET_NOT_FOUND));
    }

    //반려동물 등록
    @Transactional
    public List<PetDto.PetSimpleDto> createPetProfile(PetDto.PetRequestDto petReqDto, User user) {
        Pet pet = PetConverter.toPetEntity(petReqDto, user);
        if (petReqDto.getDeath() != null) {
            Farewell farewell = PetConverter.toNewFarewell(pet);
            pet.setFarewell(farewell);
        }
        petRepository.save(pet);
        return getPetList(user, 0, 10);
    }

    // 반려동물 목록 조회
    public List<PetDto.PetSimpleDto> getPetList(User user, int scrollPosition, int fetchSize) {
        PageRequest pageRequest = PageRequest.of(scrollPosition, fetchSize);
        Slice<Pet> petSlice = petRepository.findByUser(user, pageRequest);
        return petSlice.getContent().stream()
                .map(PetConverter::toSimpleDto)
                .toList();
    }

    @Transactional
    public Pet updatePetProfile(Integer petId, PetDto.PetRequestDto petRequestDto, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        if (!pet.getUser().getName().equals(username)) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        // 죽였다가 살린 경우 → farewell 삭제
        if (pet.getDeath() != null && petRequestDto.getDeath() == null && pet.getFarewell() != null) {
            farewellRepository.delete(pet.getFarewell());
            pet.setFarewell(null);
        }
        // 다시 죽인 경우 → farewell 없으면 생성
        if (pet.getDeath() == null && petRequestDto.getDeath() != null && pet.getFarewell() == null) {
            Farewell farewell = PetConverter.toNewFarewell(pet);
            pet.setFarewell(farewell);
        }
        pet.setName(petRequestDto.getName());
        pet.setPetCase(petRequestDto.getPetCase());
        pet.setBirth(petRequestDto.getBirth());
        pet.setDeath(petRequestDto.getDeath());
        pet.setColor(petRequestDto.getColor());
        return petRepository.save(pet);
    }

    // 반려동물 삭제
    public void deletePet(Integer petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        if (!pet.getUser().getName().equals(username)) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        petRepository.delete(pet);
    }


    @Transactional(readOnly = true)
    public PetDto.PetResponseDto getPetById(Integer petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));

        if (!pet.getUser().getName().equals(username)) {
            throw new GeneralException(ErrorCode.BAD_REQUEST);
        }
        return PetConverter.fromEntity(pet);
    }
}
