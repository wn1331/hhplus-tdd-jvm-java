package io.hhplus.tdd.utils;

import io.hhplus.tdd.global.CustomGlobalException;
import io.hhplus.tdd.global.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class PointValidator {

    public void positiveCheck(long amount){
        if (amount <= 0) {
            throw new CustomGlobalException(ErrorCode.NON_POSITIVE_INPUT);
        }
    }

    public void maxPointCheck(long resultPoint){
        if(resultPoint > 5000L){
            throw new CustomGlobalException(ErrorCode.MAX_POINT_ARRIVED);
        }
    }

    public void notEnoughPointCheck(long resultPoint){
        if (resultPoint < 0) {
            throw new CustomGlobalException(ErrorCode.NOT_ENOUGH_POINT);
        }
    }

}
