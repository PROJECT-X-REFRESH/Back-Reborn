package com.reborn.back.review.recollection.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.recollection.dto.RecollectionDto;
import com.reborn.back.review.recollection.repository.RecordRepository;
import com.reborn.back.review.recollection.repository.RemindRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            boolean didRecord = recordRepository.existsByPetAndCreatedAtBetween(pet, dayStart, dayEnd);
            weeklyList.add(new RecollectionDto(currentDate, didRemind, didRecord));
        }
        return weeklyList;
    }
}
