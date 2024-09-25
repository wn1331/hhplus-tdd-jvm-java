package io.hhplus.tdd.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.hhplus.tdd.global.CustomGlobalException;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

    /**
     * 유저 아이디 분리한 이유 : 통합테스트를 동시 실행 시 포인트가 이어짐. (테스트 간의 상태 공유)
     */
    private final long USER_ID_1 = 1L;
    private final long USER_ID_2 = 2L;
    private final long USER_ID_3 = 3L;


    @Test
    @Order(1)
    @DisplayName("[성공] 5개의_동시_충전_또는_사용_요청_테스트")
    void concurrent_charge_and_use_requests_success() {

        // 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> pointService.charge(USER_ID_1, 2000)),
            CompletableFuture.runAsync(() -> pointService.use(USER_ID_1, 1000)),
            CompletableFuture.runAsync(() -> pointService.charge(USER_ID_1, 1500)),
            CompletableFuture.runAsync(() -> pointService.use(USER_ID_1, 700)),
            CompletableFuture.runAsync(() -> pointService.charge(USER_ID_1, 1000))
        ).join();

        UserPoint result = pointService.search(USER_ID_1);

        assertEquals(result.point(), 2000 - 1000 + 1500 - 700 + 1000);

    }

    @Test
    @Order(2)
    @DisplayName("[성공] 5개의_동시_충전_요청_테스트")
    void concurrent_charge_requests_exceeding_max_balance() {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            tasks.add(CompletableFuture.runAsync(() -> pointService.charge(USER_ID_2, 500L)));
        }

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
            tasks.toArray(new CompletableFuture[0]));
        allTasks.join();

        UserPoint result = pointService.search(USER_ID_2);

        assertEquals(result.point(), 500 + 500 + 500 + 500 + 500);
    }


    @Test
    @Order(3)
    @DisplayName("[실패] 1개의_충전_요청과_4개의_동시_사용_요청_테스트_잔고_부족")
    void concurrent_use_requests_insufficient_balance() {
        // 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> pointService.charge(USER_ID_3, 5000)),
            CompletableFuture.runAsync(() -> pointService.use(USER_ID_3, 1500)),
            CompletableFuture.runAsync(() -> pointService.use(USER_ID_3, 1500)),
            CompletableFuture.runAsync(() -> pointService.use(USER_ID_3, 1500)),
            CompletableFuture.runAsync(() -> { // 여기서 잔고부족
                try {
                    pointService.use(USER_ID_3, 1500);
                } catch (CustomGlobalException e) {
                    assertEquals("포인트가 부족합니다.", e.getMessage());
                }
            })
        ).join();

        UserPoint result = pointService.search(USER_ID_3);

        assertEquals(result.point(), 5000 - 1500 - 1500 - 1500);

    }
}
