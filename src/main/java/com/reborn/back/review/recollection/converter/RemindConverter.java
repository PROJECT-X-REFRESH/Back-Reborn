package com.reborn.back.review.recollection.converter;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Recollection;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.review.recollection.dto.RemindDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RemindConverter {

    public static RemindDto.RemindResDto toResDto(Remind r, String name) {
        return RemindDto.RemindResDto.builder()
                .id(r.getId())
                .userName(name)
                .petName(r.getPet().getName())
                .title(r.getName())
                .content(r.getContents())
                .createdAt(r.getCreatedAt())
                .build();
    }
    public static Remind toRemind(RemindDto.RemindReqDto dto, Pet pet, Recollection recollection) {
        Remind remind = new Remind();
        remind.setName(dto.getTitle());
        remind.setContents(dto.getContent());
        remind.setPet(pet);
        remind.setRecollection(recollection);
        return remind;
    }

    public static Remind updateRemind(Remind entity, RemindDto.RemindReqDto dto) {
        entity.setName(dto.getTitle());
        entity.setContents(dto.getContent());
        return entity;
    }

    public static RemindDto.RemindSimpleResDto toSimpleResDto(Remind r) {
        return RemindDto.RemindSimpleResDto.builder()
                .id(r.getId())
                .title(r.getName())
                .build();
    }

    public static List<RemindDto.RemindSimpleResDto> remindSimpleListDto(List<Remind> list) {
        return list.stream().map(RemindConverter::toSimpleResDto).toList();
    }
}
