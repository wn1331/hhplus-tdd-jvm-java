package io.hhplus.tdd.unit;

import io.hhplus.tdd.point.controller.PointController;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[단위 테스트] PointController")
@WebMvcTest(PointController.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PointControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

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
    @DisplayName("[성공] 유저_포인트_사용_내역_조회")
    void test2() {
        // given
        // when
        // then
    }

    @Test
    @Order(3)
    @DisplayName("[성공] 유저_포인트_충전")
    void test3() {
        // given
        // when
        // then
    }

    @Test
    @Order(4)
    @DisplayName("[성공] 유저_포인트_사용")
    void test4() {
        // given
        // when
        // then
    }
}



