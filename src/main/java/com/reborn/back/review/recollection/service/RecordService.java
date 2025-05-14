package com.reborn.back.review.recollection.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.recollection.dto.RecordDto;
import com.reborn.back.review.recollection.mapper.RecordConverter;
import com.reborn.back.review.recollection.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
    private final RedisUtil redisUtil;
    private final RecordRepository recordRepository;
    private final PetRepository petRepository;

    public boolean checkTodayRecord(String username, Pet pet) {
        String existing = redisUtil.getData("record" + username + pet.getId());
        return existing != null;
    }

    public Integer createRecord(Integer petId, RecordDto recordDto, User user) {
        String key = "record" + user.getName() + petId;
        String existing = redisUtil.getData(key);
        if (existing != null) {  // 널이 아니면 create 자체를 막아야 함
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Record is exist.");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();
        redisUtil.setDataExpire(key, "", secondsUntilMidnight);
        Pet pet = petRepository.findById(petId).get();
        Record record = RecordConverter.toRecord(recordDto, pet);
        Record savedRecord = recordRepository.save(record);
        return savedRecord.getId();
    }

    public RecordDto updateRecord(Integer recordId, RecordDto recordDto, User user) {
        Record record = findById(recordId);
        String key = "record" + user.getName() + record.getPet().getId();
        String existing = redisUtil.getData(key);
        if (existing == null) {  // 널이면 update 자체를 막아야 함
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Record is exist.");
        }
        Record updatedRecord = RecordConverter.updateRecord(record, recordDto);
        Record savedRecord = recordRepository.save(updatedRecord);
        return RecordConverter.toDto(savedRecord);
    }

    public Record findById(Integer recordId) {
        return recordRepository.findById(recordId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Record not found"));
    }

    public void deleteRecord(Integer recordId, User user, Integer petId) {
        String key = "record" + user.getName() + petId;
        String existing = redisUtil.getData(key);

        if (existing != null) {
            // Redis 키 제거 후 DB 삭제
            redisUtil.deleteData(key);
            recordRepository.deleteById(recordId);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "오늘 안 함");
        }
    }

    public List<Record> getRecordList(Integer petId, int scrollPosition, int fetchSize) {
        Pageable pageable = PageRequest.of(scrollPosition, fetchSize, Sort.by("createdAt").descending());
        Pet pet = petRepository.findById(petId).get();
        List<Record> records = recordRepository.findByPet(pet, pageable);
        return records;
    }
}
