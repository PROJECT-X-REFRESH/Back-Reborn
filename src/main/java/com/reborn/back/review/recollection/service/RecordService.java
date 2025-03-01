package com.reborn.back.review.recollection.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.review.recollection.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    public boolean checkTodayRecord(Pet pet) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 23:59:59

        return recordRepository.existsByPetAndCreatedAtBetween(pet, startOfDay, endOfDay);
    }

}
