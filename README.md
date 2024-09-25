<h1>[STEP 2 - 동시성 제어 방식에 대한 분석 및 보고서]</h1>

참조한 레퍼런스 : https://devwithpug.github.io/java/java-thread-safe/

자바에서의 동시성 해결방법 : syncronized(Lock), Atomic

대표적으로 
멀티쓰레드 환경(특히 스프링) 에서는 여러 스레드가 변경 가능한 공유 데이터를 동시에 수정하려 할 때 경쟁조건(Race Condition)이 발생한다.
자바에서는 이러한 경쟁조건을 회피할 수 있도록 syncronized 기능을 제공한다.
이 키워드를 붙이면 해당 블록에는 오직 하나의 쓰레드만 접근가능하게 된다.
