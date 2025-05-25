package com.reborn.back.review.recollection.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodayRecollctDto {
    Integer remindId;
    Integer recordId;
}