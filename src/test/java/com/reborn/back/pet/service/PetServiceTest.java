package com.reborn.back.pet.service;

import com.reborn.back.domain.entity.PetColor;
import com.reborn.back.domain.entity.PetType;
import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.farewell.Farewell;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.pet.dto.PetRequestDto;
import com.reborn.back.pet.dto.PetResponseDto;
import com.reborn.back.pet.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.reborn.back.domain.entity.PetColor.BLACK;
import static com.reborn.back.domain.entity.PetColor.BROWN;
import static com.reborn.back.domain.entity.PetType.DOG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class PetServiceTest {

    private PetService petService;
    private PetRepository petRepository;

    private User mockUser;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        petService = new PetService(petRepository);

        mockUser = User.builder()
                .name("testUser")
                .uid("testUid")
                .build();
    }

    @Test
    @DisplayName("펫 프로필 생성 - 정상 시나리오")
    void createPetProfile_Success() {
        // given
        PetRequestDto petRequestDto = PetRequestDto.builder()
                .name("Buddy")
                .petCase(DOG)
                .birth(LocalDate.of(2020, 1, 2))
                .color(BROWN)
                .build();

        Pet savedPet = Pet.builder()
                .name("Buddy")
                .petCase(DOG)
                .birth(LocalDate.of(2020, 1, 2))
                .color(BROWN)
                .user(mockUser)
                .farewell(Farewell.builder().step(0).build())
                .build();

        given(petRepository.save(any(Pet.class))).willReturn(savedPet);
        given(petRepository.findByUser(eq(mockUser), any(PageRequest.class)))
                .willReturn(new SliceImpl<>(List.of(savedPet)));

        // when
        List<PetResponseDto> response = petService.createPetProfile(petRequestDto, mockUser);

        // then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Buddy", response.get(0).getName());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("펫 목록 조회 - 정상 시나리오")
    void getPetList_Success() {
        // given
        Pet pet = Pet.builder()
                .name("Buddy")
                .petCase(DOG)
                .birth(LocalDate.of(2020, 1, 2))
                .color(BROWN)
                .user(mockUser)
                .build();

        given(petRepository.findByUser(eq(mockUser), any(PageRequest.class)))
                .willReturn(new SliceImpl<>(List.of(pet)));

        // when
        List<PetResponseDto> response = petService.getPetList(mockUser, 0, 10);

        // then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Buddy", response.get(0).getName());
    }

    @Test
    @DisplayName("펫 프로필 업데이트 - 정상 시나리오")
    void updatePetProfile_Success() {
        // given
        PetRequestDto petRequestDto = PetRequestDto.builder()
                .name("Buddy Updated")
                .petCase(DOG)
                .birth(LocalDate.of(2018, 1, 2))
                .death(LocalDate.of(2024, 1, 2))
                .color(BLACK)
                .build();

        Pet existingPet = Pet.builder()
                .id(1)
                .name("Buddy")
                .user(mockUser)
                .build();

        given(petRepository.findById(1)).willReturn(Optional.of(existingPet));
        given(petRepository.save(any(Pet.class))).willReturn(existingPet);

        // when
        Pet updatedPet = petService.updatePetProfile(1, petRequestDto, "testUser");

        // then
        assertNotNull(updatedPet);
        assertEquals("Buddy Updated", updatedPet.getName());
        verify(petRepository).save(existingPet);
    }

    @Test
    @DisplayName("펫 프로필 업데이트 - 권한 없는 경우 예외 발생")
    void updatePetProfile_Unauthorized() {
        // given
        PetRequestDto petRequestDto = PetRequestDto.builder()
                .name("Buddy")
                .build();

        Pet existingPet = Pet.builder()
                .id(1)
                .name("Buddy")
                .user(mockUser)
                .build();

        given(petRepository.findById(1)).willReturn(Optional.of(existingPet));

        // when & then
        GeneralException ex = assertThrows(GeneralException.class,
                () -> petService.updatePetProfile(1, petRequestDto, "wrongUser"));

        assertEquals(ErrorCode.BAD_REQUEST.getCode(), ex.getErrorCode());
    }

    @Test
    @DisplayName("펫 삭제 - 정상 시나리오")
    void deletePet_Success() {
        // given
        Pet pet = Pet.builder()
                .id(1)
                .name("Buddy")
                .user(mockUser)
                .build();

        given(petRepository.findById(1)).willReturn(Optional.of(pet));
        doNothing().when(petRepository).delete(any(Pet.class));

        // when
        petService.deletePet(1, "testUser");

        // then
        verify(petRepository).delete(pet);
    }

    @Test
    @DisplayName("펫 삭제 - 없는 Pet ID 예외")
    void deletePet_NotFound() {
        // given
        given(petRepository.findById(1)).willReturn(Optional.empty());

        // when & then
        GeneralException ex = assertThrows(GeneralException.class,
                () -> petService.deletePet(1, "testUser"));

        assertEquals(ErrorCode.PET_NOT_FOUND.getCode(), ex.getErrorCode());
    }
}