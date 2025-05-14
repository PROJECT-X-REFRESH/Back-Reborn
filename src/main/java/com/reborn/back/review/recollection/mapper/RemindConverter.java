package com.reborn.back.review.recollection.mapper;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.review.recollection.dto.RemindDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RemindConverter {

    public static RemindDto.RemindResDto toResDto(Remind r) {
        return RemindDto.RemindResDto.builder()
                .id(r.getId())
                .title(r.getName())
                .content(r.getContents())
                .createdAt(r.getCreatedAt())
                .build();
    }

    public static List<RemindDto.RemindResDto> remindListDto(List<Remind> list) {
        return list.stream().map(RemindConverter::toResDto).toList();
    }

    public static Remind toRemind(RemindDto.RemindReqDto dto, Pet pet) {
        return new Remind(null, dto.getTitle(), dto.getContent(), pet);
    }

    public static Remind updateRemind(Remind entity, RemindDto.RemindReqDto dto) {
        entity.setName(dto.getTitle());
        entity.setContents(dto.getContent());
        return entity;
    }
}
