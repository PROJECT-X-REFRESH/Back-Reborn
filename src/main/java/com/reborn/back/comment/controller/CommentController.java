package com.reborn.back.comment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "comment", description = "댓글 관련 api입니다")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
}
