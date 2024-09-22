package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.domain.PointHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository{

    private final PointHistoryTable pointHistoryTable;

    public List<PointHistory> selectAllByUserId(Long id) {
        return pointHistoryTable.selectAllByUserId(id);
    }

    @Override
    public PointHistory insert(Long id, Long amount, TransactionType type,
        Long updatemillis) {
        return pointHistoryTable.insert(id,amount,type,updatemillis);
    }
}

