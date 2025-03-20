package com.reborn.back.review.recollection.controller;

import com.reborn.back.domain.review.recollection.Record;
import com.reborn.back.domain.review.recollection.Remind;
import com.reborn.back.domain.user.User;
import com.reborn.back.global.api.ApiResponse;
import com.reborn.back.global.api.SuccessCode;
import com.reborn.back.login.auth.mapper.CustomUserDetails;
import com.reborn.back.login.service.UserService;
import com.reborn.back.review.recollection.dto.RecordDto;
import com.reborn.back.review.recollection.service.RecordService;
import com.reborn.back.review.recollection.mapper.RecordConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "record", description = "record 관련")
@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordController {

    private final UserService userService;
    private final RecordService recordService;

    @Operation(summary = "record 생성", description = "record 생성하는 API")
    @PostMapping(value = "{petId}/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Integer> createRecord(
            @PathVariable Integer petId,
            @RequestBody RecordDto recordDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) throws IOException {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Integer recordId = recordService.createRecord(petId, recordDto, user);
        return ApiResponse.onSuccess(SuccessCode.RECORD_CREATED, recordId);
    }

    @Operation(summary = "record 수정", description = "record 내용을 수정하는 API") // 작성자만 수정 가능
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOARD_2002", description = "게시물 수정이 완료되었습니다.")
    })
    @PutMapping("/{recordId}")
    public ApiResponse<RecordDto> updateRemind(
            @PathVariable Integer recordId,
            @RequestBody RecordDto recordDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        return ApiResponse.onSuccess(SuccessCode.RECORD_UPDATED, recordService.updateRecord(recordId, recordDto, user));
    }

    @Operation(summary = "record 목록 조회", description = "record 목록을 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECORD_2004", description = "게시물 목록 조회가 완료되었습니다.")
    })
    @Parameters({
            @Parameter(name = "scrollPosition", description = "가져올 데이터의 시작 위치 (0부터 시작)"),
            @Parameter(name = "fetchSize", description = "한 번에 불러올 게시글 개수")
    })
    @PostMapping("/list/{petId}/{scrollPosition}/{fetchSize}")
    public ApiResponse<List<RecordDto>> getListRecords(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Integer petId,
            @RequestParam(name = "scrollPosition", defaultValue = "0") int scrollPosition,
            @RequestParam(name = "fetchSize", defaultValue = "50") int fetchSize
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        List<Record> records = recordService.getRecordList(petId, scrollPosition, fetchSize);
        return ApiResponse.onSuccess(SuccessCode.RECORD_LIST_VIEW_SUCCESS, RecordConverter.recordListDto(records));
    }

    @Operation(summary = "record 상세 조회", description = "특정 record 조회하는 API")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECORD_2001", description = "게시물 상세 조회가 완료되었습니다.")
    })
    @GetMapping("/{recordId}")
    public ApiResponse<RecordDto> getRecord(
            @PathVariable Integer recordId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        Record record = recordService.findById(recordId);
        return ApiResponse.onSuccess(SuccessCode.RECORD_DETAIL_VIEW_SUCCESS, RecordConverter.toDto(record));
    }

    @Operation(summary = "record 삭제", description = "기록을 삭제하는 API (오늘만 가능)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "RECORD_2003", description = "게시물 삭제가 완료되었습니다.")
    })
    @DeleteMapping("/{petId}/{recordId}")
    public ApiResponse<Boolean> deleteRecord(
            @PathVariable Integer petId,
            @PathVariable Integer recordId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findUserByUserName(customUserDetails.getUsername());
        recordService.deleteRecord(recordId, user, petId);
        return ApiResponse.onSuccess(SuccessCode.RECORD_DELETED, true);
    }

}