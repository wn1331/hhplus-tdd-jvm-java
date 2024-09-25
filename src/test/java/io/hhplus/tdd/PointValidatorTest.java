package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.hhplus.tdd.global.CustomGlobalException;
import io.hhplus.tdd.global.ErrorCode;
import io.hhplus.tdd.utils.PointValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@DisplayName("[단위 테스트] PointValidator")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PointValidatorTest {

    private PointValidator pointValidator;

    @BeforeEach
    void setUp() {
        pointValidator = new PointValidator();
    }

    @Test
    @Order(1)
    @DisplayName("[실패] 양수 체크 - 0 이하의 값 입력")
    void positiveCheck_negative_or_zero_amount() {
        // 0 이하의 값에 대해 예외가 발생하는지 테스트 (분기)
        assertThatThrownBy(() -> pointValidator.positiveCheck(-100L))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NON_POSITIVE_INPUT);

        assertThatThrownBy(() -> pointValidator.positiveCheck(0L))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NON_POSITIVE_INPUT);
    }

    @Test
    @Order(2)
    @DisplayName("[실패] 최대 포인트 체크 - 5000 초과")
    void maxPointCheck_exceeds_max_point() {
        // 5000L 초과하는 값에 대해 예외가 발생하는지 테스트
        assertThatThrownBy(() -> pointValidator.maxPointCheck(6000L))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MAX_POINT_ARRIVED);
    }

    @Test
    @Order(3)
    @DisplayName("[실패] 잔여 포인트 체크 - 잔여 포인트 부족")
    void notEnoughPointCheck_insufficient_point() {
        // 0 미만의 값에 대해 예외가 발생하는지 테스트
        assertThatThrownBy(() -> pointValidator.notEnoughPointCheck(-500L))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ENOUGH_POINT);
    }

}
