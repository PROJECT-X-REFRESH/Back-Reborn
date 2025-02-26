package com.reborn.back.mypage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "mypage", description = "")
@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MypageController {
}
