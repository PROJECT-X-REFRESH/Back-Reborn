package com.reborn.back.review.recollection.mapper;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.review.recollection.dto.RecordDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RecordConverter {

    public static RecordDto.RecordResDto toResDto(Record r) {
        return RecordDto.RecordResDto.builder()
                .id(r.getId())
                .title(r.getTitle())
                .content(r.getContent())
                .emotion(r.getEmotion())
                .createdAt(r.getCreatedAt())
                .build();
    }

    public static List<RecordDto.RecordResDto> recordListDto(List<Record> list) {
        return list.stream().map(RecordConverter::toResDto).toList();
    }

    public static Record toRecord(RecordDto.RecordReqDto dto, Pet pet) {
        return new Record(null, dto.getTitle(), dto.getContent(), dto.getEmotion(), pet);
    }

    public static Record updateRecord(Record entity, RecordDto.RecordReqDto dto) {
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setEmotion(dto.getEmotion());
        return entity;
    }
}