package com.reborn.back.review.recollection.service;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Recollection;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ErrorCode;
import com.reborn.back.global.exception.GeneralException;
import com.reborn.back.global.utils.Redis.RedisUtil;
import com.reborn.back.pet.repository.PetRepository;
import com.reborn.back.review.recollection.dto.RemindDto;
import com.reborn.back.review.recollection.converter.RemindConverter;
import com.reborn.back.review.recollection.repository.RemindRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private final RecollectionService recollectionService;

    public boolean checkTodayRemind(String username, Pet pet) {
        String existing = redisUtil.getData("remind" + username + pet.getId());
        return existing != null;
    }

    public Integer createRemind(Integer petId, RemindDto.RemindReqDto dto, User user) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        if (pet.getFarewell() != null || pet.getDeath() != null) {
            throw new GeneralException(ErrorCode.PET_ALREADY_DEAD);
        }
        String key = "remind" + user.getName() + petId;
        if (redisUtil.getData(key) != null)
            throw new GeneralException(ErrorCode.REMIND_ALREADY_EXISTS_TODAY);

        long ttl = Duration.between(LocalDateTime.now(),
                        LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay())
                .getSeconds();
        redisUtil.setDataExpire(key, "", ttl);
        Recollection recollection = recollectionService.getOrCreateRecollection(pet);
        Remind entity = RemindConverter.toRemind(dto, pet, recollection);
        return remindRepository.save(entity).getId();
    }

    public RemindDto.RemindSimpleResDto updateRemind(Integer remindId, RemindDto.RemindReqDto dto, User user) {

        Remind remind = findById(remindId);
        String key = "remind" + user.getName() + remind.getPet().getId();
        if (redisUtil.getData(key) == null)
            throw new  GeneralException(ErrorCode.REMIND_NOT_WRITE_TODAY);

        Remind updated = RemindConverter.updateRemind(remind, dto);
        return RemindConverter.toSimpleResDto(remindRepository.save(updated));
    }

    public RemindDto.RemindResDto getRemind(Integer id, User user) {
        return RemindConverter.toResDto(findById(id), user.getNickname());
    }

    public List<RemindDto.RemindSimpleResDto> getRemindList(Integer petId, int scrollPosition, int fetchSize) {

        Pageable page = PageRequest.of(scrollPosition, fetchSize, Sort.by("createdAt").descending());
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() ->  new GeneralException(ErrorCode.REMIND_NOT_FOUND));

        return RemindConverter.remindSimpleListDto(remindRepository.findByPet(pet, page));
    }

    public Remind findById(Integer id) {
        return remindRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorCode.REMIND_NOT_FOUND));
    }

    public void deleteRemind(Integer remindId, User user, Integer petId) {
        String key = "remind" + user.getNickname() + petId;
        if (redisUtil.getData(key) == null)
            throw new GeneralException(ErrorCode.REMIND_NOT_WRITE_TODAY);

        redisUtil.deleteData(key);
        remindRepository.deleteById(remindId);
    }

    public RemindDto.RemindInfoDto getRemindInfo(Integer petId, User user) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new GeneralException(ErrorCode.PET_NOT_FOUND));
        return RemindDto.RemindInfoDto.builder()
                .userName(user.getNickname())
                .petName(pet.getName())
                .build();
    }
}