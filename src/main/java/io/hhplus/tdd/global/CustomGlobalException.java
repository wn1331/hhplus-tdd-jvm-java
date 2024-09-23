package io.hhplus.tdd.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomGlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    @Override
    public String getMessage(){
        return errorCode.getMessage();
    }

}
