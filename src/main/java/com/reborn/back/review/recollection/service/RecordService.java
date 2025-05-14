package com.reborn.back.review.recollection.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordService {

    private final RedisUtil redisUtil;
    private final RecordRepository recordRepository;
    private final PetRepository petRepository;

    public boolean checkTodayRecord(String username, Pet pet) {
        return redisUtil.getData("record" + username + pet.getId()) != null;
    }

    public Integer createRecord(Integer petId, RecordDto.RecordReqDto dto, User user) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() ->new GeneralException(ErrorCode.PET_NOT_FOUND));
        if (pet.getFarewell() != null || pet.getDeath() != null) {
            throw new GeneralException(ErrorCode.PET_ALREADY_DEAD);
        }
        String key = "record" + user.getName() + petId;
        if (redisUtil.getData(key) != null)
            throw new GeneralException(ErrorCode.RECORD_ALREADY_EXISTS_TODAY);
        long ttl = Duration.between(LocalDateTime.now(),
                        LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay())
                .getSeconds();
        redisUtil.setDataExpire(key, "", ttl);
        Record entity = RecordConverter.toRecord(dto, pet);
        return recordRepository.save(entity).getId();
    }

    public RecordDto.RecordResDto updateRecord(Integer recordId, RecordDto.RecordReqDto dto, User user) {
        Record record = findById(recordId);
        String key = "record" + user.getName() + record.getPet().getId();
        if (redisUtil.getData(key) == null)
            throw new GeneralException(ErrorCode.RECORD_NOT_WRITE_TODAY);
        Record updated = RecordConverter.updateRecord(record, dto);
        return RecordConverter.toResDto(recordRepository.save(updated));
    }

    public Record findById(Integer id) {
        return recordRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.RECORD_NOT_FOUND));
    }

    public RecordDto.RecordResDto getRecord(Integer recordId) {
        Record entity = findById(recordId);
        return RecordConverter.toResDto(entity);
    }

    public void deleteRecord(Integer recordId, User user, Integer petId) {
        String key = "record" + user.getName() + petId;
        if (redisUtil.getData(key) == null)
            throw new GeneralException(ErrorCode.RECORD_NOT_WRITE_TODAY);
        redisUtil.deleteData(key);
        recordRepository.deleteById(recordId);
    }

    public List<RecordDto.RecordResDto> getRecordList(Integer petId, int scrollPosition, int fetchSize) {
        Pageable page = PageRequest.of(scrollPosition, fetchSize, Sort.by("createdAt").descending());
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        List<Record> records = recordRepository.findByPet(pet, page);
        return RecordConverter.recordListDto(records);
    }
}