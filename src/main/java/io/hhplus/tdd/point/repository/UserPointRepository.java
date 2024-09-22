package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.domain.UserPoint;

public interface UserPointRepository {

    UserPoint selectById(Long id);

    UserPoint insertOrUpdate(Long id, Long amount);

}
