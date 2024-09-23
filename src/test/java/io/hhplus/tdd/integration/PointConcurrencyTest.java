package io.hhplus.tdd.integration;

import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("[통합 테스트] 포인트 충전/사용 동시성 테스트")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PointConcurrencyTest {

    @Autowired
    private PointService pointService;

    @Test
    @Order(1)
    @DisplayName("[성공] 5개의_동시_충전_요청_테스트")
    void test1() {
        // given
        // when
        // then
    }

    @Test
    @Order(2)
    @DisplayName("[실패] 5개의_동시_충전_요청_테스트_최대_잔고_초과")
    void test2() {
        // given
        // when
        // then
    }

    @Test
    @Order(3)
    @DisplayName("[실패] 5개의_동시_사용_요청_테스트_잔고_부족")
    void test3() {
        // given
        // when
        // then
    }
}
