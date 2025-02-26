package com.reborn.back.board.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "board", description = "board 관련 api.")
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
}
