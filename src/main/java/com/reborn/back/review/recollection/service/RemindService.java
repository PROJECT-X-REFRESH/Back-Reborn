package com.reborn.back.review.recollection.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.recollection.dto.RemindDto;
import com.reborn.back.review.recollection.mapper.RemindConverter;
import com.reborn.back.review.recollection.repository.RemindRepository;
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
public class RemindService {
    private final RedisUtil redisUtil;
    private final RemindRepository remindRepository;
    private final PetRepository petRepository;

    public boolean checkTodayRemind(String username, Pet pet) {
        String existing = redisUtil.getData("remind" + username + pet.getId());
        return existing != null;
    }

    public Integer createRemind(Integer petId, RemindDto remindDto, User user) {
        String key = "remind" + user.getName() + petId;
        String existing = redisUtil.getData(key);
        if (existing != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Remind is exist.");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();
        redisUtil.setDataExpire(key, "", secondsUntilMidnight);
        Pet pet = petRepository.findById(petId).get();
        Remind remind = RemindConverter.toRemind(remindDto, pet);
        Remind savedRemind = remindRepository.save(remind);
        return savedRemind.getId();
    }

    public RemindDto updateRemind(Integer remindId, RemindDto remindDto, User user) {
        Remind remind = findById(remindId);
        String key = "remind" + user.getName() + remind.getPet().getId();
        String existing = redisUtil.getData(key);
        if (existing == null) {  // 널이면 update 자체를 막아야 함
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Remind is exist.");
        }
        Remind updatedRemind = RemindConverter.updateRemind(remind, remindDto);
        Remind savedRemind = remindRepository.save(updatedRemind);
        return RemindConverter.toDto(savedRemind);
    }

    public Remind findById(Integer remindId) {
        return remindRepository.findById(remindId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Remind not found"));

    }

    public void deleteRemind(Integer remindId, User user, Integer petId) {
        String existing = redisUtil.getData("remind" + user.getName() + petId);

        if (existing != null) {
            redisUtil.deleteData("remind" + user.getName() + petId);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "오늘 안 함");
        }
    }

    public List<Remind> getRemindList(Integer petId, int scrollPosition, int fetchSize) {
        Pageable pageable = PageRequest.of(scrollPosition, fetchSize, Sort.by("createdAt").descending());
        Pet pet = petRepository.findById(petId).get();
        List<Remind> reminds = remindRepository.findByPet(pet, pageable);
        return reminds;
    }
}
