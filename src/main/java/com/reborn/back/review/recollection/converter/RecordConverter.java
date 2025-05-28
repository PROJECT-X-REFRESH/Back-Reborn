package com.reborn.back.review.recollection.converter;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Recollection;
import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.review.recollection.dto.RecordDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RecordConverter {

    public static RecordDto.RecordResDto toResDto(Record r) {
        return RecordDto.RecordResDto.builder()
                .id(r.getId())
                .content(r.getContent())
                .emotion(r.getEmotion())
                .createdAt(r.getCreatedAt())
                .build();
    }

    public static List<RecordDto.RecordSimpleResDto> recordListDto(List<Record> list) {
        return list.stream().map(RecordConverter::toResSimpleDto).toList();
    }

    public static Record toRecord(RecordDto.RecordReqDto dto, Pet pet, Recollection recollection) {
        Record record = new Record();
        record.setContent(dto.getContent());
        record.setEmotion(dto.getEmotion());
        record.setPet(pet);
        record.setRecollection(recollection);
        return record;
    }

    public static Record updateRecord(Record entity, RecordDto.RecordReqDto dto) {
        entity.setContent(dto.getContent());
        entity.setEmotion(dto.getEmotion());
        return entity;
    }

    public static RecordDto.RecordSimpleResDto toResSimpleDto(Record r) {
        return RecordDto.RecordSimpleResDto.builder()
                .id(r.getId())
                .createdAt(r.getCreatedAt())
                .build();
    }

}