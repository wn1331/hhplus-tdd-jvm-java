package io.hhplus.tdd.point.service;


import io.hhplus.tdd.global.CustomGlobalException;
import io.hhplus.tdd.global.ErrorCode;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.utils.LockManager;
import java.util.List;
import java.util.concurrent.locks.Lock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private static final long MAX_POINT = 5000L;

    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;
    private final LockManager lockManager;

    // 유저 포인트 조회
    public UserPoint search(long id) {
        return userPointRepository.selectById(id);
    }

    // 유저 히스토리 조회
    public List<PointHistory> findHistory(long id) {
        return pointHistoryRepository.selectAllByUserId(id);
    }

    // 유저 포인트 충전
    public UserPoint charge(long id, long amount) {
        // 포인트 정책, 입력값은 음수이거나 0일 수 없음
        if (amount <= 0) {
            throw new CustomGlobalException(ErrorCode.NON_POSITIVE_INPUT);
        }
        Lock userLock = lockManager.getLock(id);
        userLock.lock();
        try{
            UserPoint userPoint = userPointRepository.selectById(id);

            long resultPoint = userPoint.point() + amount;

            // 포인트 정책, 최대 포인트 잔고는 5000포인트를 넘을 수 없다.
            if (resultPoint > MAX_POINT) {
                throw new CustomGlobalException(ErrorCode.MAX_POINT_ARRIVED);
            }
            pointHistoryRepository.insert(id, amount, TransactionType.CHARGE);
            return userPointRepository.insertOrUpdate(id, resultPoint);
        }finally {
            userLock.unlock();
        }
    }

    // 유저 포인트 사용
    public UserPoint use(long id, long amount) {

        // 포인트 정책, 입력값은 음수이거나 0일 수 없음
        if (amount <= 0) {
            throw new CustomGlobalException(ErrorCode.NON_POSITIVE_INPUT);
        }

        Lock userLock = lockManager.getLock(id);
        userLock.lock();
        try {

            UserPoint userPoint = userPointRepository.selectById(id);

            // 포인트 정책, 포인트 부족
            long resultPoint = userPoint.point() - amount;
            if (resultPoint < 0) {
                throw new CustomGlobalException(ErrorCode.NOT_ENOUGH_POINT);
            }

            pointHistoryRepository.insert(id, amount, TransactionType.USE);
            return userPointRepository.insertOrUpdate(id, resultPoint);
        }finally {
            userLock.unlock();
        }
    }


}
