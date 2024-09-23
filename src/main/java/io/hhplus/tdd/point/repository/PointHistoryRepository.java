package io.hhplus.tdd.point.repository;


import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.PointHistory;
import java.util.List;

public interface PointHistoryRepository {
    List<PointHistory> selectAllByUserId(Long id);

    PointHistory insert(Long id, Long amount, TransactionType type);

}
