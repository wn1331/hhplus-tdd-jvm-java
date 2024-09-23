package io.hhplus.tdd.unit;

import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("[단위 테스트] PointService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PointServiceUnitTest {

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    PointService pointService;

    @Test
    @Order(1)
    @DisplayName("[성공] 유저_포인트_조회")
    void test1() {
        // given
        // when
        // then
    }

    @Test
    @Order(2)
    @DisplayName("[성공] 유저_포인트_조회_유저_없음")
    void test2() {
        // given
        // when
        // then
    }

    @Test
    @Order(3)
    @DisplayName("[성공] 유저_포인트_내역_조회")
    void test3() {
        // given
        // when
        // then
    }

    @Test
    @Order(4)
    @DisplayName("[성공] 유저_포인트_내역_조회_유저_없음")
    void test4() {
        // given
        // when
        // then
    }

    @Test
    @Order(5)
    @DisplayName("[성공] 유저_포인트_충전")
    void test5() {
        // given
        // when
        // then
    }

    @Test
    @Order(6)
    @DisplayName("[실패] 유저_포인트_충전_입력값_오류")
    void test6() {
        // given
        // when
        // then
    }

    @Test
    @Order(7)
    @DisplayName("[실패] 유저_포인트_충전_최대_잔고_초과")
    void test7() {
        // given
        // when
        // then
    }

    @Test
    @Order(8)
    @DisplayName("[성공] 유저_포인트_사용")
    void test8() {
        // given
        // when
        // then
    }

    @Test
    @Order(9)
    @DisplayName("[실패] 유저_포인트_사용_입력값_오류")
    void test9() {
        // given
        // when
        // then
    }

    @Test
    @Order(10)
    @DisplayName("[실패] 유저_포인트_사용_잔고_부족")
    void test10() {
        // given
        // when
        // then
    }

}
