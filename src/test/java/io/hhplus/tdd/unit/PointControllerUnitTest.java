package io.hhplus.tdd.unit;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.controller.PointController;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("[단위 테스트] PointController")
@WebMvcTest(PointController.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class PointControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    private UserPoint userPoint;
    private final long USER_ID = 1L;
    private final long AMOUNT = 500L;
    private final String POST_URL = "/point/{id}";

    @BeforeEach
    void setUp() {
        // UserPoint 객체를 초기화
        userPoint = new UserPoint(USER_ID, AMOUNT, System.currentTimeMillis());
    }

    @Test
    @Order(1)
    @DisplayName("[성공] 유저_포인트_조회")
    void user_point_search_test() throws Exception {
        // given
        given(pointService.search(USER_ID)).willReturn(userPoint); // 조회한 포인트가 500
        // when
        ResultActions resultActions = mockMvc.perform(get(POST_URL, USER_ID));
        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(USER_ID))
            .andExpect(jsonPath("$.point").value(AMOUNT));
    }

    @Test
    @Order(2)
    @DisplayName("[성공] 포인트_사용_내역_조회")
    void point_history_search_test() throws Exception {
        // given
        List<PointHistory> pointHistoryList = IntStream.rangeClosed(1, 8)
            .mapToObj(id -> {
                if(id%2==0){
                    return new PointHistory(
                        id,
                        USER_ID,
                        AMOUNT,
                        TransactionType.CHARGE,
                        System.currentTimeMillis()
                    );
                }
                else{
                    return new PointHistory(
                        id,
                        USER_ID,
                        AMOUNT,
                        TransactionType.USE,
                        System.currentTimeMillis()
                    );
                }
            }).toList(); // CHARGE와 USE 번갈아서 생성
        given(pointService.findHistory(USER_ID)).willReturn(pointHistoryList);

        // when
        ResultActions resultActions = mockMvc.perform(get(POST_URL + "/histories", USER_ID));
        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(pointHistoryList.size())); // 응답의 리스트 길이가 일치하는지 확인.
    }

    @Test
    @Order(3)
    @DisplayName("[성공] 유저_포인트_충전")
    void user_point_charge_test() throws Exception {
        // given
        given(pointService.charge(USER_ID,AMOUNT)).willReturn(userPoint); // 충전한 결과가 500
        // when
        ResultActions resultActions = mockMvc.perform(patch(POST_URL + "/charge", USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.valueOf(AMOUNT))
        );
        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(USER_ID))
           .andExpect(jsonPath("$.point").value(userPoint.point()));
    }

    @Test
    @Order(4)
    @DisplayName("[성공] 유저_포인트_사용")
    void user_point_use_test() throws Exception {
        // given
        given(pointService.use(USER_ID,AMOUNT)).willReturn(userPoint); // 사용하고 남은 포인트가 500
        // when
        ResultActions resultActions = mockMvc.perform(patch(POST_URL+"/use",USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(String.valueOf(AMOUNT)));
        // then
        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userPoint.id()))
            .andExpect(jsonPath("$.point").value(userPoint.point()));


    }
}




