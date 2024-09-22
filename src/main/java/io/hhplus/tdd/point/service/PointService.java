package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointHistoryRepository pointHistoryRepository;
    private final UserPointRepository userPointRepository;


    // 유저 포인트 조회
    public UserPoint search(long id){

        return null;
    }

    // 유저 포인트 충전
    public UserPoint charge(Long id, Long amount) {

        return null;
    }

    // 유저 히스토리 조회
    public List<PointHistory> findHistory(long id){
        return null;
    }

    // 유저 포인트 사용
    public UserPoint use(long id, long amount){
        return null;
    }

}
