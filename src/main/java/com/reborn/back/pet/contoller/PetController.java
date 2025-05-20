package com.reborn.back.pet.contoller;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.pet.dto.PetRequestDto;
import com.reborn.back.pet.dto.PetResponseDto;
import com.reborn.back.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@Tag(name = "반려동물", description = "반려동물 관련 api")
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {
    private final UserService userService;
    private final PetService petService;

    @Operation(summary = "펫 프로필 만들기", description = "펫 프로필을 생성하는 api.")
    @PostMapping(value = "/profile/create")
    public ApiResponse<List<PetResponseDto>> createPetProfile(
            @RequestBody PetRequestDto petReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<PetResponseDto> petList = petService.createPetProfile(petReqDto, user);
        return ApiResponse.onSuccess(SuccessCode.PET_CREATE_SUCCESS, petList);
    }

    @Operation(summary = "반려동물 조회", description = "사용자의 반려동물의 상세정보를 조회합니다.")
    @GetMapping("/{petId}")
    public ApiResponse<PetResponseDto> getPetDetail(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int petId
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        PetResponseDto pet = petService.getPetById(petId, user.getName());
        return ApiResponse.onSuccess(SuccessCode.PET_LIST_VIEW_SUCCESS, pet);
    }

    @Operation(summary = "반려동물 목록 조회", description = "사용자의 반려동물 목록을 스크롤 기반으로 조회합니다.")
    @GetMapping("/list/{scrollPosition}")
    public ApiResponse<List<PetResponseDto>> getPetList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "10") int fetchSize
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<PetResponseDto> petList = petService.getPetList(user, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.PET_LIST_VIEW_SUCCESS, petList);
    }

    /**
     * 반려동물 정보 수정
     */
    @Operation(summary = "반려동물 수정", description = "반려동물 정보를 수정합니다.")
    @PutMapping("/update/{petId}")
    public ApiResponse<Integer> updatePet(
            @PathVariable Integer petId,
            @RequestBody PetRequestDto petRequestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Pet updatedPet = petService.updatePetProfile(petId, petRequestDto, customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.PET_UPDATED, updatedPet.getId());
    }

    @Operation(summary = "반려동물 삭제", description = "반려동물 정보를 삭제합니다.")
    @DeleteMapping("/delete/{petId}")
    public ApiResponse<Void> deletePet(
            @PathVariable Integer petId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        petService.deletePet(petId, customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.PET_DELETED, null);
    }
}
