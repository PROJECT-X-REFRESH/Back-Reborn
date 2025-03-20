package com.reborn.back.review.recollection.mapper;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.user.User;
import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.review.recollection.dto.RecordDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor

public class RecordConverter {
    public static List<RecordDto> recordListDto(List<Record> records) {
        return records.stream()
                .map(record -> RecordDto.builder()
                        .id(record.getId())
                        .title(record.getTitle())
                        .content(record.getContent())
                        .createdAt(record.getCreatedAt())
                        .build())
                .toList();
    }


    public static Record toRecord(RecordDto recordDto, Pet pet, User user) {
        return new Record(pet, user, recordDto.getTitle(), recordDto.getContent());
    }

    public static Record updateRecord(Record record, RecordDto recordDto) {
        record.setTitle(recordDto.getTitle());
        record.setContent(recordDto.getContent());
        return record;
    }

    public static RecordDto toDto(Record record) {
        return RecordDto.builder()
                .id(record.getId())
                .title(record.getTitle())
                .content(record.getContent())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
