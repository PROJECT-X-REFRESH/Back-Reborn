package com.reborn.back.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseCode { // 성공
    OK(HttpStatus.OK, "COMMON_200", "Success"),
    CREATED(HttpStatus.CREATED, "COMMON_201", "Created"),

    USER_LOGIN_SUCCESS(HttpStatus.CREATED, "USER_2011", "회원가입& 로그인이 완료되었습니다."),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "USER_2001", "로그아웃 되었습니다."),
    USER_REISSUE_SUCCESS(HttpStatus.OK, "USER_2002", "토큰 재발급이 완료되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER_2003", "회원탈퇴가 완료되었습니다."),
    USER_PHONE_VERIFICATION_SUCCESS(HttpStatus.OK, "USER_2004", "전화번호 인증이 완료되었습니다."),
    USER_INFO_SUCCESS(HttpStatus.OK, "USER_2008", "개인 정보 열람이 완료되었습니다."),
    USER_NICKNAME_SUCCESS(HttpStatus.OK, "USER_2009", "닉네임 생성이 완료되었습니다."),
    MAIN_INFO_SUCCESS(HttpStatus.OK, "USER_2010", "메인 화면의 개인 정보 열람이 완료되었습니다."),

    USER_PROFILE_IMAGE_UPDATED(HttpStatus.OK, "USER_2004", "프로필 사진 업데이트가 완료되었습니다."),
    USER_BACKGROUND_IMAGE_UPDATED(HttpStatus.OK, "USER_2005", "배경 사진 업데이트가 완료되었습니다."),
    USER_PROFILE_IMAGE_BROWSE(HttpStatus.OK, "USER_2006", "프로필 사진 열람이 완료되었습니다."),
    USER_BACKGROUND_IMAGE_BROWSE(HttpStatus.OK, "USER_2007", "배경 사진 열람이 완료되었습니다."),

    FILE_DELETE_SUCCESS(HttpStatus.OK, "FILE_2001", "파일 삭제가 완료되었습니다."),

    FCM_SEND_SUCCESS(HttpStatus.OK, "FCM_2001", "알림 전송이 완료되었습니다."),

    RECONNECT_CREATED(HttpStatus.CREATED, "RECONNECT_2011", "나의 반려동물과 만나기 생성이 완료되었습니다."),
    RECONNECT_TO_BE_CREATED(HttpStatus.CREATED, "RECONNECT_2012", "반려동물이 없습니다."),
    RECONNECT_GOODBYE(HttpStatus.CREATED, "RECONNECT_2013", "작별하러 가기가 완료되었습니다."),

    PET_LIST_VIEW_SUCCESS(HttpStatus.OK, "PET_2001", "반려동물 정보 목록 조회가 완료되었습니다."),
    PET_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "PET_2002", "반려동물 정보 조회가 완료되었습니다."),
    PET_DELETED(HttpStatus.OK, "PET_2003", "반려동물 삭제가 완료되었습니다."),
    PET_NAME_VIEW_SUCCESS(HttpStatus.OK, "PET_2004", "반려동물 이름 조회가 완료되었습니다."),

    REVIEW_RECONNECT_VIEW_SUCCESS(HttpStatus.OK, "REVIEW_2001", "나의 반려동물과 만나기 조회가 완료되었습니다."),
    REVIEW_REMIND_VIEW_SUCCESS(HttpStatus.OK, "REVIEW_2002", "충분한 대화 나누기 내용 조회가 완료되었습니다."),
    REVIEW_REVEAL_VIEW_SUCCESS(HttpStatus.OK, "REVIEW_2003", "나의 감정 들여다보기 내용 조회가 완료되었습니다."),
    REVIEW_REMEMBER_VIEW_SUCCESS(HttpStatus.OK, "REVIEW_2004", "건강한 작별 준비하기 내용 조회가 완료되었습니다."),
    REVIEW_REBORN_VIEW_SUCCESS(HttpStatus.OK, "REVIEW_2005", "건강한 작별하기 내용 조회가 완료되었습니다."),
    REVIEW_REMIND_CHECK_SUCCESS(HttpStatus.OK, "REVIEW_2006", "충분한 대화 나누기 존재 확인이 완료되었습니다."),
    REVIEW_REVEAL_CHECK_SUCCESS(HttpStatus.OK, "REVIEW_2007", "나의 감정 들여다보기 존재 확인이 완료되었습니다."),
    REVIEW_REMEMBER_CHECK_SUCCESS(HttpStatus.OK, "REVIEW_2008", "건강한 작별 준비하기 존재 확인이 완료되었습니다."),
    REVIEW_REBORN_CHECK_SUCCESS(HttpStatus.OK, "REVIEW_2009", "건강한 작별하기 존재 확인이 완료되었습니다."),


    BOARD_CREATED(HttpStatus.CREATED, "BOARD_2011", "게시판 생성이 완료되었습니다."),
    BOARD_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "BOARD_2001", "게시판 상세 조회가 완료되었습니다."),
    BOARD_UPDATED(HttpStatus.OK, "BOARD_2002", "게시판 수정이 완료되었습니다."),
    BOARD_DELETED(HttpStatus.OK, "BOARD_2003", "게시판 삭제가 완료되었습니다."),
    BOARD_LIST_VIEW_SUCCESS(HttpStatus.OK, "BOARD_2004", "전체 게시판 리스트 조회가 완료되었습니다."),
    BOARD_LIKE_CHECK_SUCCESS(HttpStatus.OK, "BOARD_2005", "사용자가 좋아요 누름여부 확인 완료되었습니다."),
    BOARD_BOOKMARK_CHECK_SUCCESS(HttpStatus.OK, "BOARD_2006", "사용자가 북마크 누름여부 확인 완료되었습니다."),

    BOARD_LIKE_SUCCESS(HttpStatus.OK, "LIKE_2001", "게시판 좋아요가 완료되었습니다."),
    BOARD_UNLIKE_SUCCESS(HttpStatus.OK, "LIKE_2002", "게시판 좋아요 취소가 완료되었습니다."),
    BOARD_LIKE_COUNT_SUCCESS(HttpStatus.OK, "LIKE_2003", "게시판 좋아요 개수 조회가 완료되었습니다."),

    BOARD_BOOKMARK_SUCCESS(HttpStatus.OK, "BOOKMARK_2001", "게시판 북마크가 완료되었습니다."),
    BOARD_UNBOOKMARK_SUCCESS(HttpStatus.OK, "BOOKMARK_2002", "게시판 북마크 취소가 완료되었습니다."),

    COMMENT_CREATED(HttpStatus.CREATED, "COMMENT_2011", "댓글 생성이 완료되었습니다."),
    COMMENT_DELETED(HttpStatus.OK, "COMMENT_2001", "댓글 삭제가 완료되었습니다."),
    COMMENT_LIST_VIEW_SUCCESS(HttpStatus.OK, "BOARD_2002", "댓글 리스트 조회가 완료되었습니다."),

    REDIARY_CREATED(HttpStatus.CREATED, "REDIARY_2011", "감정 일기 생성이 완료되었습니다."),
    REDIARY_LIST_VIEW_SUCCESS(HttpStatus.OK, "REDIARY_2001", "감정 일기 목록 조회가 완료되었습니다."),
    REDIARY_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "REDIARY_2002", "감정 일기 조회가 완료되었습니다."),
    REDIARY_UPDATED(HttpStatus.OK, "REDIARY_2003", "감정 일기 수정이 완료되었습니다."),
    REDIARY_DELETED(HttpStatus.OK, "REDIARY_2004", "감정 일기 삭제가 완료되었습니다."),
    REDIARY_TODAY_WRITTEN_CHECKD(HttpStatus.OK, "REDIARY_2005", "당일 감정일기 작성한 여부 알려줍니다."),

    REVEAL_CREATED(HttpStatus.CREATED, "REVEAL_2011", "나의 감정 들여다보기 생성이 완료되었습니다."),
    REVEAL_LIST_VIEW_SUCCESS(HttpStatus.OK, "REVEAL_2001", "나의 감정 들여다보기 목록 조회가 완료되었습니다."),
    REVEAL_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "REVEAL_2002", "나의 감정 들여다보기 조회가 완료되었습니다."),
    REVEAL_WRITE_COMPLETED(HttpStatus.OK, "REVEAL_2003", "일기 작성이 완료되었습니다."),
    REVEAL_PAT_COMPLETED(HttpStatus.OK, "REVEAL_2004", "쓰다듬기가 완료되었습니다."),
    REVEAL_FEED_COMPLETED(HttpStatus.OK, "REVEAL_2005", "밥주기가 완료되었습니다."),
    REVEAL_WALK_COMPLETED(HttpStatus.OK, "REVEAL_2006", "산책하기가 완료되었습니다."),
    REVEAL_SNACK_COMPLETED(HttpStatus.OK, "REVEAL_2007", "간식주기가 완료되었습니다."),
    REVEAL_INTRO_COMPLETED(HttpStatus.OK, "REVEAL_2008", "쓰다듬기로 넘어가기가 완료되었습니다."),
    REVEAL_PLAY_COMPLETED(HttpStatus.OK, "REVEAL_2009", "놀아주기가 완료되었습니다."),

    REMIND_CREATED(HttpStatus.CREATED, "REMIND_2011", "충분한 대화 나누기 생성이 완료되었습니다."),
    REMIND_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "REMIND_2002", "충분한 대화 나누기 조회가 완료되었습니다."),
    REMIND_WRITE_COMPLETED(HttpStatus.OK, "REMIND_2003", "답변 작성이 완료되었습니다."),
    REMIND_PAT_COMPLETED(HttpStatus.OK, "REMIND_2004", "쓰다듬기가 완료되었습니다."),
    REMIND_FEED_COMPLETED(HttpStatus.OK, "REMIND_2005", "밥주기가 완료되었습니다."),
    REMIND_WALK_COMPLETED(HttpStatus.OK, "REMIND_2006", "산책하기가 완료되었습니다."),
    REMIND_SNACK_COMPLETED(HttpStatus.OK, "REMIND_2007", "간식주기가 완료되었습니다."),
    REMIND_INTRO_COMPLETED(HttpStatus.OK, "REMIND_2008", "쓰다듬기로 넘어가기가 완료되었습니다."),
    REMIND_PLAY_COMPLETED(HttpStatus.OK, "REMIND_2009", "놀아주기가 완료되었습니다."),

    REMEMBER_CREATED(HttpStatus.CREATED, "REMEMBER_2011", "건강한 작별 준비하기 생성이 완료되었습니다."),
    REMEMBER_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "REMEMBER_2002", "건강한 작별 준비하기 조회가 완료되었습니다."),
    REMEMBER_WRITE_COMPLETED(HttpStatus.OK, "REMEMBER_2003", "그림일기 작성이 완료되었습니다."),
    REMEMBER_PAT_COMPLETED(HttpStatus.OK, "REMEMBER_2004", "쓰다듬기가 완료되었습니다."),
    REMEMBER_FEED_COMPLETED(HttpStatus.OK, "REMEMBER_2005", "밥주기가 완료되었습니다."),
    REMEMBER_WALK_COMPLETED(HttpStatus.OK, "REMEMBER_2006", "산책하기가 완료되었습니다."),
    REMEMBER_SNACK_COMPLETED(HttpStatus.OK, "REMEMBER_2007", "간식주기가 완료되었습니다."),
    REMEMBER_CLEAN_COMPLETED(HttpStatus.OK, "REMEMBER_2008", "정리가 완료되었습니다."),
    REMEMBER_INTRO_COMPLETED(HttpStatus.OK, "REMEMBER_2009", "쓰다듬기로 넘어가기가 완료되었습니다."),
    REMEMBER_PLAY_COMPLETED(HttpStatus.OK, "REMEMBER_2010", "놀아주기가 완료되었습니다."),

    REBORN_CREATED(HttpStatus.CREATED, "REBORN_2011", "건강한 작별하기 생성이 완료되었습니다."),
    REBORN_DETAIL_VIEW_SUCCESS(HttpStatus.OK, "REBORN_2002", "건강한 작별하기 조회가 완료되었습니다."),
    REBORN_WRITE_COMPLETED(HttpStatus.OK, "REBORN_2003", "작별인사 작성이 완료되었습니다."),
    REBORN_PAT_COMPLETED(HttpStatus.OK, "REBORN_2004", "쓰다듬기가 완료되었습니다."),
    REBORN_FEED_COMPLETED(HttpStatus.OK, "REBORN_2005", "밥주기가 완료되었습니다."),
    REBORN_WASH_COMPLETED(HttpStatus.OK, "REBORN_2006", "씻겨주기가 완료되었습니다."),
    REBORN_CLOTHE_COMPLETED(HttpStatus.OK, "REBORN_2007", "옷 입혀주기가 완료되었습니다."),
    REBORN_FINISH_COMPLETED(HttpStatus.OK, "REBORN_2008", "15일 콘텐츠가 완료되었습니다."),
    REBORN_SET_REBORN_COMPLETED(HttpStatus.OK, "REBORN_2009", "리본 선택이 완료되었습니다."),
    REBORN_INTRO_COMPLETED(HttpStatus.OK, "REBORN_2010", "쓰다듬기로 넘어가기가 완료되었습니다."),
    REBORN_OUTRO_COMPLETED(HttpStatus.OK, "REBORN_2011", "아웃트로로 넘어가기가 완료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    // 응답 코드 상세 정보 return
    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .code(this.code)
                .message(this.message)
                .build();
    }
}