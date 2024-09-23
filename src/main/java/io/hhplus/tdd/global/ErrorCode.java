package io.hhplus.tdd.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NON_POSITIVE_INPUT("E100",HttpStatus.BAD_REQUEST,"입력값은 음수이거나 0일 수 없습니다."),
    NOT_ENOUGH_POINT("E101", HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    MAX_POINT_ARRIVED("E102", HttpStatus.BAD_REQUEST, "최대 포인트 잔고(5000)를 넘을 수 없습니다.");

    private final String code;
    private final HttpStatus statusCode;
    private final String message;

}
