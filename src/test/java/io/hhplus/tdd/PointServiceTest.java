package io.hhplus.tdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import io.hhplus.tdd.global.CustomGlobalException;
import io.hhplus.tdd.global.ErrorCode;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.utils.LockManager;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
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
class PointServiceTest {

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @Mock
    LockManager lockManager;

    @InjectMocks
    PointService pointService;

    private final long USER_ID = 1L;
    private final long AMOUNT = 1000L;
    private UserPoint userPoint;
    private final Lock userLock = new ReentrantLock();

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(USER_ID, AMOUNT, System.currentTimeMillis());

    }

    @Test
    @Order(1)
    @DisplayName("[성공] 유저_포인트_조회")
    void user_point_search_test() {
        // given
        given(userPointRepository.selectById(USER_ID)).willReturn(userPoint);

        // when
        UserPoint actualUserPoint = pointService.search(USER_ID);
        // then
        assertThat(actualUserPoint.id()).isEqualTo(USER_ID);
        assertThat(actualUserPoint.point()).isEqualTo(AMOUNT);
    }

    @Test
    @Order(2)
    @DisplayName("[성공] 유저_포인트_조회_유저_없음")
    void user_point_search_test_no_user() {
        // given
        given(userPointRepository.selectById(USER_ID)).willReturn(
            new UserPoint(USER_ID, 0L, System.currentTimeMillis()));

        // when
        UserPoint actualUserPoint = pointService.search(USER_ID);
        // then
        assertThat(actualUserPoint.id()).isEqualTo(USER_ID);
        assertThat(actualUserPoint.point()).isZero(); // 없는 유저는 포인트가 0.
    }

    @Test
    @Order(3)
    @DisplayName("[성공] 유저_포인트_내역_조회")
    void point_history_test() {
        // given
        List<PointHistory> pointHistoryList = IntStream.rangeClosed(1, 8)
            .mapToObj(id -> {
                if (id % 2 == 1) {
                    return new PointHistory(
                        id,
                        USER_ID,
                        AMOUNT,
                        TransactionType.CHARGE,
                        System.currentTimeMillis()
                    );
                } else {
                    return new PointHistory(
                        id,
                        USER_ID,
                        AMOUNT,
                        TransactionType.USE,
                        System.currentTimeMillis()
                    );
                }
            }).toList(); // CHARGE와 USE 번갈아서 생성

        given(pointHistoryRepository.selectAllByUserId(USER_ID)).willReturn(pointHistoryList);

        // when
        List<PointHistory> actualPointHistoryList = pointService.findHistory(USER_ID);

        // then
        assertEquals(pointHistoryList.size(), actualPointHistoryList.size());
    }

    @Test
    @Order(4)
    @DisplayName("[성공] 유저_포인트_내역_조회_유저_없음")
    void point_history_test_no_user() {
        // given
        List<PointHistory> pointHistoryList = Collections.emptyList(); // 없는 유저의 내역을 조회하면 빈 리스트를 반환한다.

        given(pointHistoryRepository.selectAllByUserId(USER_ID)).willReturn(pointHistoryList);
        // when
        List<PointHistory> actualPointHistoryList = pointService.findHistory(USER_ID);

        // then
        assertThat(actualPointHistoryList).isEmpty();

    }

    @Test
    @Order(5)
    @DisplayName("[성공] 유저_포인트_충전")
    void user_point_charge_test() {
        // given
        given(lockManager.getLock(USER_ID)).willReturn(userLock);
        PointHistory pointHistory = new PointHistory(1L, USER_ID, 500L, TransactionType.CHARGE,
            System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(USER_ID, AMOUNT + 500L,
            System.currentTimeMillis());
        given(userPointRepository.selectById(USER_ID)).willReturn(userPoint);
        given(userPointRepository.insertOrUpdate(USER_ID, AMOUNT + 500L)).willReturn(
            updatedUserPoint); // 500 포인트 충전
        given(pointHistoryRepository.insert(USER_ID, 500L, TransactionType.CHARGE)).willReturn(
            pointHistory); // 충전 내역 저장

        // when
        UserPoint actualUserPoint = pointService.charge(USER_ID, 500L); // 500포인트 충전

        // then
        assertThat(actualUserPoint.id()).isEqualTo(USER_ID);
        assertThat(actualUserPoint.point()).isEqualTo(AMOUNT + 500L); // 500 포인트를 더한 1000 포인트인지 확인

    }

    @Test
    @Order(6)
    @DisplayName("[실패] 유저_포인트_충전_입력값_오류_음수값")
    void user_point_charge_test_negative_amount() {
        // given
        long amount = -100L;
        // when & then
        assertThatThrownBy(() -> pointService.charge(USER_ID, amount))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NON_POSITIVE_INPUT);
    }

    @Test
    @Order(7)
    @DisplayName("[실패] 유저_포인트_충전_입력값_오류_0값")
    void user_point_charge_test_zero_amount() {
        // given
        long amount = 0L;
        // when & then
        assertThatThrownBy(() -> pointService.charge(USER_ID, amount))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NON_POSITIVE_INPUT);
    }

    @Test
    @Order(8)
    @DisplayName("[실패] 유저_포인트_충전_최대_잔고_초과")
    void user_point_charge_test_max_point() {
        // given
        long amount = 4500L; //기존 1000 + 4500 (최대잔고 5000)
        given(lockManager.getLock(USER_ID)).willReturn(userLock);

        given(userPointRepository.selectById(USER_ID)).willReturn(userPoint);

        // when & then
        assertThatThrownBy(() -> pointService.charge(USER_ID, amount))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MAX_POINT_ARRIVED);

    }

    @Test
    @Order(9)
    @DisplayName("[성공] 유저_포인트_사용")
    void user_point_use_test() {
        // given
        long useAmount = 500L;
        given(lockManager.getLock(USER_ID)).willReturn(userLock);

        UserPoint updatedUserPoint = new UserPoint(USER_ID, AMOUNT - useAmount,
            System.currentTimeMillis());

        given(userPointRepository.selectById(USER_ID)).willReturn(userPoint);
        given(userPointRepository.insertOrUpdate(USER_ID, AMOUNT - useAmount)).willReturn(
            updatedUserPoint); // 2000 -> 1000

        // when
        UserPoint actualUserPoint = pointService.use(USER_ID, useAmount);

        // then
        assertThat(actualUserPoint.id()).isEqualTo(updatedUserPoint.id());
        assertThat(actualUserPoint.point()).isEqualTo(updatedUserPoint.point());
    }


    @Test
    @Order(10)
    @DisplayName("[실패] 유저_포인트_사용_입력값_오류_음수값")
    void user_point_use_test_negative_amount() {
        // given
        long invalidAmount = -100L;

        // when & then
        assertThatThrownBy(() -> pointService.use(USER_ID, invalidAmount))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NON_POSITIVE_INPUT);
    }

    @Test
    @Order(11)
    @DisplayName("[실패] 유저_포인트_사용_입력값_오류_0값")
    void user_point_use_test_zero_amount() {
        // given
        long invalidAmount = 0L;

        // when & then
        assertThatThrownBy(() -> pointService.use(USER_ID, invalidAmount))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NON_POSITIVE_INPUT);
    }

    @Test
    @Order(12)
    @DisplayName("[실패] 유저_포인트_사용_잔고_부족")
    void user_point_use_test_not_enough_point() {
        // given
        long amount = 3000L;
        given(lockManager.getLock(USER_ID)).willReturn(userLock);

        given(userPointRepository.selectById(USER_ID)).willReturn(userPoint);

        // when & then
        assertThatThrownBy(() -> pointService.use(USER_ID, amount))
            .isInstanceOf(CustomGlobalException.class)
            .hasFieldOrPropertyWithValue("errorCode", ErrorCode.NOT_ENOUGH_POINT);
    }



}
