package com.reborn.back.review.recollection.service;

import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
import com.reborn.back.domain.entity.EmotionState;
import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Recollection;
import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.recollection.dto.RecollectionDto;
import com.reborn.back.review.recollection.dto.TodayRecollctDto;
import com.reborn.back.review.recollection.repository.RecollectionRepository;
import com.reborn.back.review.recollection.repository.RecordRepository;
import com.reborn.back.review.recollection.repository.RemindRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecollectionService {
    private final RecollectionRepository recollectionRepository;
    private final RecordRepository recordRepository;
    private final RemindRepository remindRepository;
    private final PetRepository petRepository;

    public List<RecollectionDto> getThisWeekList(User user, Integer petId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atStartOfDay();
        Pet pet = petRepository.findById(petId).get();
        List<RecollectionDto> weeklyList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.toLocalDate().plusDays(i);
            LocalDateTime dayStart = currentDate.atStartOfDay();
            LocalDateTime dayEnd = currentDate.atTime(23, 59, 59, 999_999_999);
            boolean didRemind = remindRepository.existsByPetAndCreatedAtBetween(pet, dayStart, dayEnd);
            EmotionState emotionState = null;
            boolean didRecord = false;
            var optionalRecord = recordRepository.findTopByPetAndCreatedAtBetweenOrderByCreatedAtDesc(pet, dayStart, dayEnd);
            if (optionalRecord.isPresent()) {
                didRecord = true;
                emotionState = optionalRecord.get().getEmotion().getState();
            }
            weeklyList.add(new RecollectionDto(currentDate, didRemind, didRecord, emotionState));
        }
        return weeklyList;
    }

    @Transactional
    public Recollection getOrCreateRecollection(Pet pet) {
        return recollectionRepository.findByPet(pet)
                .orElseGet(() -> recollectionRepository.save(
                        Recollection.builder()
                                .pet(pet)
                                .build()));
}

    public Integer checkRecollection(User user, Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        return recollectionRepository.findByPet(pet)
                .map(Recollection::getId)
                .orElse(null);
    }

    public TodayRecollctDto getTodayList(User user, Integer petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        LocalDate today = LocalDate.now();
        LocalDateTime dayStart = today.atStartOfDay();
        LocalDateTime dayEnd = today.atTime(23, 59, 59, 999_999_999);
        Integer remindId = remindRepository.findTopByPetAndCreatedAtBetweenOrderByCreatedAtDesc(pet, dayStart, dayEnd)
                .map(Remind::getId)
                .orElse(null);
        Integer recordId = recordRepository.findTopByPetAndCreatedAtBetweenOrderByCreatedAtDesc(pet, dayStart, dayEnd)
                .map(Record::getId)
                .orElse(null);
        return TodayRecollctDto.builder()
                .remindId(remindId)
                .recordId(recordId)
                .build();
    }
}