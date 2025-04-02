package com.reborn.back.review.recollection.mapper;

import com.reborn.back.domain.pet.Pet;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.domain.user.User;
import com.reborn.back.review.recollection.dto.RemindDto;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class RemindConverter {
    public static List<RemindDto> remindListDto(List<Remind> reminds) {
        return reminds.stream()
                .map(remind -> RemindDto.builder()
                        .id(remind.getId())
                        .title(remind.getName())
                        .content(remind.getContents())
                        .createdAt(remind.getCreatedAt())
                        .build())
                .toList();
    }


    public static Remind toRemind(RemindDto remindDto, Pet pet) {
        return new Remind(null, remindDto.getTitle(), remindDto.getContent(), pet);
    }

    public static Remind updateRemind(Remind remind, RemindDto remindDto) {
        remind.setName(remindDto.getTitle());
        remind.setContents(remindDto.getContent());
        return remind;
    }

    public static RemindDto toDto(Remind remind) {
        return RemindDto.builder()
                .id(remind.getId())
                .title(remind.getName())
                .content(remind.getContents())
                .createdAt(remind.getCreatedAt())
                .build();
    }
}
