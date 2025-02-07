package com.reborn.back.mypage.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsVerifyRequest {
    private String phoneNum;
    private String confirmNum;

}
