<h1>[STEP 2 - 동시성 제어 방식에 대한 분석 및 보고서]</h1>

대표적으로 
멀티쓰레드 환경(특히 스프링) 에서는 여러 스레드가 변경 가능한 공유 데이터를 동시에 수정하려 할 때 경쟁조건(Race Condition)이 발생한다.
자바에서는 이러한 경쟁조건을 회피할 수 있도록 여러가지 동기화 대응 방안들이 있다.


<h2>DB 없는 단일 인스턴스 환경에서의 동시성 대응 방안</h2>
동시성 제어에는 Syncronized, LockSupport, ReentrantLock에 대해 먼저 공부했다.
<h3>syncronized 키워드</h3>
자바에서 가장 기본적인 동기화 방법이다. 특정 코드 블록이나 메서드에 대해 한 번에 하나의 스레드만 접근할 수 있도록 보장한다.
장점으로는 , 프로그래밍 언어에 문법으로 제공한다는 것과, 편리하게 사용할 수 있다는 점, 그리고 자동 잠금 해제가 가능하다는 것이다.
<br> 자동 잠금 해제는 syncronized 메서드난 블록이 완료되면 자동으로 락을 대기중인 다른 스레드의 잠금이 해제된다. 개발자가 직접 특정 스레드를 깨우도록 관리해야 한다면, 매우 어렵고 번거로울 것이다.
<br>하지만 큰 단점이 존재하는데, 바로 무한 대기 문제와 데드락 문제와 공정성 문제이다. 
<p>무한 대기 상태: 한 스레드가 syncronized 블록을 장기간 점유하게 되면 다른 스레드들이 영원히 대기할 수 있다. BLOCKED 상태의 쓰레드는 락이 풀릴때까지 무한 대기하는 특징 때문 </p>
<p>공정성 문제 : 락이 돌아왔을 때 BLOCKED 상태의 여러 쓰레드 중에 어떤 쓰레드가 락을 획득할 지 알 수가 없다. 최악의 경우에는 특정 스레드가 너무 오랜기간 락을 획득하지 못할 수도 있다고 한다. </p>

syncronized의 가장 치명적인 단점은 락을 얻기 위해 BLOCKED 상태가 되면 락을 얻을 때까지 무한 대기한다는 점이다. 예를 들어 고객이 어떤 요청을 했는데, 화면에 계속 요청 중만 뜨고, 응답을 못 받는 것이다. 
<br> 결국, 더 유연하고 더 세밀한 제어가 가능한 방법들이 필요하게 되었다. 이런 문제를 해결하기 위해 자바5부터 java.util.concurrent라는 동시성 문제 해결을 위한 패키지가 추가된다.

단순하고 편리하게 사용하기에는 참 좋다. 목적에 맞게 쓰도록 하는것이 좋을 것 같다.

<h3>LockSupport</h3>
syncronized의 단점을 보완하기 위해 자바 5에 나타난 녀석.
쓰레드를 WAITING 상태로 변경한다. WAITING 상태는 누가 깨워주기 전까지는 계속 대기한다. 그리고 CPU 실행 스케줄링에 들어가지 않닫다.
저수준의 락 관리 기능을 제공한다. 특히 쓰레드를 직접 차단하고 깨우는 기능이 있어서 무한 대기 문제를 해결할 수 있다고 한다.
LockSupport의 존재 자체도 몰랐는데, 팀원분께서 이런 것도 있다고 하셔서 좋은 공부가 되어가고 있다. 
장점으로는 인터럽트를 사용할 수 있다는 것이다. WAITING 상태의 쓰레드에 인터럽트가 발생하면 RUNNABLE 상태로 변하면서 깨어난다.
<br>이것도 단점이 있다. 바로 저수준의 기능만 존재한다는 것이다.
<p>저수준의 기능 : 매!우 저수준의 락 관리 매커니즘을 제공해서 코드가 복잡해질 수 있다고 한다.</p>
무슨 기능이 있는지 알아보자.
<li>park() : 쓰레드를 WAITING(대기) 상태로 변경한다. </li>
<li>parkNanos(나노초) : 쓰레드를 나노초동안만 TIMED_WAITING 상태로 변경한다.</li>
<li>unPark(쓰레드) : WAITING 상태의 대상 쓰레드를 RUNNABLE 상태로 변경한다.</li>

<h3>ReentrantLock</h3>
자바 5부터 도입된 고급 락 매커니즘으로 syncronized의 단점을 보완하기 위해 나타난 녀석. BLOCKED 상태를 통한 임계 영역 관리의 한계를 극복하기 위해 만들어졌다고 한다.
특징으로는 무한대기 방지, 공정성, 다양한 기능이 있다.
<p>무한 대기 방지 : tryLock 메서드를 사용해서 일정 시간 동안만 락을 시도하고, 실패할 경우 대기하지 않도록 할 수 있다.(무한대기 회피 전략)</p>
<p>공정성 : 공정성을 보장할지의 여부를 설정할 수 있다. 공정성이 설정되면 락을 먼저 요청한 스레드가 우선적으로 락을 획들할 수 있도록 한다.</p>
<p>다양한 기능 : syncronized보다 훨씬 많은 기능이 내장되어 있다. 락을 명시적으로 획득,해제 가능하다, 재진입도 가능하고 대기 중인 스레드를 인터럽트하거나 위에 말했듯 시간을 설정하여 락을 시도하는것도 가능하다.</p>

자바5에서 등장한 Lock 인터페이스와 ReentrantLock 덕분에 syncronized의 단점인 무한 대기와 공정성 문제를 극복하고, 또 더욱 유연하고 세밀한 쓰레드 제어가 가능하게 되었다.


<h2>내가 구현한 동시성 대응 방안</h2>
과제 목표 중 유저별로 락을 관리할 수 있게 구현해야 한다고 써있엇다. 여러 조사를 해본 결과, ReentrantLock과 ConcurrentHashMap을 사용해서 유저별로 락을 걸 수 있도록 하였다.

각 USER_ID에 대해 별도의 락을 관리하는 것으로 단일 인스턴스에서 여러 스레드가 동일한 유저아이디에 대해 순차적으로 작업을 수행하도록 보장 가능하다.
ConcurrentHashMap은 HashMap과 같지만, 각 요소에 대한 get/set 연산이 key별로 syncronized로 제어된다. 고로 Thread-safe하다고 한다. (-허재 코치님 멘토링 중-) 
이렇게 ConcurrentHashMap을 사용한 락 관리 기능을 스프링 컴포넌트로 분리하고, 서비스 계층에서 이를 주입받아 사용하도록 구현했다.
```
@Component
public class LockManager {
    private final Map<Long, Lock> locks = new ConcurrentHashMap<>();

    public Lock getLock(Long id) {
        return locks.computeIfAbsent(id, key -> new ReentrantLock());
    }
}

```

locks.computeIfAbsent(id, key -> new ReentrantLock()); 이 코드는 id가 맵에 존재하지 않으면, 새로운 ReentrantLock을 생성하고 맵에 저장한다. id가 이미 존재하면 해당 키에 매핑된 기존의 ReentrantLock을 반환한다.

<h2>동시성 테스트하는 여러 가지 방법</h2>
<h3>1. CompletableFuture를 사용하는 방법</h3>
CompletableFuture는 자바 8부터 도입된 비동기 프로그래밍을 지원하는 클래스이다. 
비동기 작업을 수행하고 그 결과를 처리할 수 있게 해주는 도구로, 여러 스레드를 사용하여 병렬 작업을 쉽게 구현할 수 있다. 그리고 콜백을 통해 작업 완료 후 후속 작업을 정의할 수 있는 기능을 제공한다.
CompletableFuture의 join() 메서드는, 이러한 비동기 작업이 모두 마칠때까지 기다리는 메서드로, 동시성 테스트하기 위해 적합한 메서드이다.

```
        for (int i = 0; i < 5; i++) {
            tasks.add(CompletableFuture.runAsync(() -> pointService.charge(USER_ID_2, 500L)));
        }

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
            tasks.toArray(new CompletableFuture[0]));
        allTasks.join();
```






<h3>2. CountdownLatch를 이용하는 방법</h3>
CountDownLatch는 자바의 concurrent패키지에 속한 클래스 중 하나로, 여러 쓰레드가 특정 작업을 완료할 때까지 대기하게 하는 동기화 도구이다. 이를 통해 특정 조건이 만족될 때까지 한 ㅅ크레드 또는 여럿 쓰레드가 기다리도록 설정할 수 있다.
ExecutorService를 활용해서 쓰레드 풀을 선언하고, 카운트다운으로 하나씩 줄여주면서 테스트를 하면 된다.execute는 반환값 없을때, submit은 반환값 있을때 사용

```
    CountDownLatch latch = new CountDownLatch(taskCount);
    ExecutorService executorService = Executors.newFixedThreadPool(taskCount);

    // 비동기 작업 수행
    executorService.execute(() -> {
        try {
            pointService.charge(USER_ID_1, 2000);
        } finally {
            latch.countDown(); // 작업 완료 후 latch 카운트 감소
        }
    });

    executorService.execute(() -> {
        try {
            pointService.use(USER_ID_1, 1000);
        } finally {
            latch.countDown(); // 작업 완료 후 latch 카운트 감소
        }
    });
    
    // 모든 작업이 완료될 때까지 대기
    latch.await();

    // ExecutorService 종료
    executorService.shutdown();
```

<h2>동시성 테스트 시 고려 사항</h2>
1. <b>경쟁 조건(Race Condition)</b> : 두 개 이상의 작업이 동일한 자원에 접근할 때 발생할 수 있는 문제. CompletableFuture의 비동기 작업이 서로 간섭하는지 확인하려면, CompletableFuture의 결과를 검증하고 예외 상황을 시뮬레이션할 수 있다.
2. <b>데드락(DeadLock)</b> : 두 개 이상의 작업이 서로 완료를 기다리면서 영원히 멈추는 상태. CompletableFuture을 사용하여 복잡한 작업 의존성을 다룰 때 데드락이 발생하지 않도록 주의해야 한다.
3. 동시성 테스트를 위해 CountDownLatch, Semaphore, ExectutorService 등을 사용하여 작업의 시작 및 종료를 조정 가능하다. CompletableFuture와 함께 사용한다면, 테스트 환경에서 작업 간의 정확한 동기화를 유지하는 것이 중요할 수 있다.



<h3>시도했던 동시성 테스트</h3> 
CompletableFuture을 사용해서 동시에 5개의 요청을 비동기로 요청하고 join 메서드로 끝날때까지 기다렸다.(멘토링때 팁을 많이 주워담았다.)
<br> 이후에 동시성 테스트를 다른 방식으로 어떻게 구현할까 고민 중, AtomicInteger를 사용해서 성공 카운트 또는 실패 카운트로 그것을 검증하는 테스트 코드를 구현했다.

```
         // 멀티쓰레드 환경에서 동기화 없이도 정수값 업데이트 가능한 AtomicInteger
        AtomicInteger exceptionCount = new AtomicInteger(0);

        for (int i = 0; i < 10; i++) {
            tasks.add(CompletableFuture.runAsync(() -> {
                try {
                    pointService.charge(USER_ID_2, 2000L);

                } catch (CustomGlobalException e) {
                    exceptionCount.incrementAndGet(); // 8번 추가되어야 함

                    // Exception이 제대로 발생했는지 확인
                    assertEquals("최대 포인트 잔고(5000)를 넘을 수 없습니다.", e.getMessage());
                }
            }));
        }
```

이 부분에 대해 허재 코치님께 멘토링 시간에 여쭈어보았지만, 동시성 테스트에는 적합하지 않다고, 
동시성 테스트가 아닌, 비동기 테스트(카프카 등)에 적합한 메서드라는 피드백이 있었다. 

그래서 AtomicInteger는 <b>다시 제거하고</b>, 초기에 생각했던 대로 동시성 통합테스트를 구현했다.

<h3>최종적으로 제출한 동시성 테스트</h3>

```
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
```
