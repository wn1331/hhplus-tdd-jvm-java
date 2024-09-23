package io.hhplus.tdd.point.controller;

import static org.springframework.http.ResponseEntity.ok;

import io.hhplus.tdd.point.domain.PointHistory;
import io.hhplus.tdd.point.domain.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private static final Logger log = LoggerFactory.getLogger(PointController.class);
    private final PointService pointService;

    /**
     * 특정 유저의 포인트를 조회하는 기능
     */
    @GetMapping("{id}")
    public ResponseEntity<UserPoint> point(
        @PathVariable(value = "id") long id
    ) {
        return ok(pointService.search(id));
    }

    /**
     * 특정 유저의 포인트 충전/이용 내역을 조회하는 기능
     */
    @GetMapping("{id}/histories")
    public ResponseEntity<List<PointHistory>> history(
        @PathVariable long id
    ) {
        return ok(pointService.findHistory(id));
    }

    /**
     * 특정 유저의 포인트를 충전하는 기능
     */
    @PatchMapping("{id}/charge")
    public ResponseEntity<UserPoint> charge(
        @PathVariable long id,
        @RequestBody long amount
    ) {
        return ok(pointService.charge(id,amount));
    }

    /**
     * 특정 유저의 포인트를 사용하는 기능
     */
    @PatchMapping("{id}/use")
    public ResponseEntity<UserPoint> use(
        @PathVariable long id,
        @RequestBody long amount
    ) {
        return ok(pointService.use(id,amount));
    }
}
