package io.hhplus.tdd.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Component;

@Component
public class LockManager {
    private final Map<Long, Lock> locks = new ConcurrentHashMap<>();

    public Lock getLock(Long id) {
        return locks.computeIfAbsent(id, key -> new ReentrantLock());
    }
}
