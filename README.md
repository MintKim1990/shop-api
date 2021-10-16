** 멀티스레드 환경에서 동일컬럼 사용시 데이터 정합성 보장이슈     
Item 엔티티는 stockQuantity 라는 수량컬럼을 가지고있는데     
고객이 주문시 선택한 수량만큼 차감되머 취소시 증가되야한다.

발생이슈 (동시에 2고객이 Book을 주문)
1. Book 조회
고객A가 Book을 10개 주문 (Book 정보 조회시 남은수량 30개인 Book 엔티티 리턴)
고객B가 Book을 20개 주문 (Book 정보 조회시 남은수량 30개인 Book 엔티티 리턴)
   
2. 주문 생성 (Book 수량 차감)
고객A가 Book 10개를 주문하여 수량 30개가 남은 Book 엔티티에 수량차감
메소드를 호출하여 30개에서 -10만큼 수량이 차감되며 변경감지에 의해 Update 실행
   
고객B가 Book 20개를 주문하여 수량 30개가 남은 Book 엔티티에 수량차감
메소드를 호출하여 20개에서 -20만큼 수량이 차감되며 변경감지에 의해 Update 실행

3. 문제 (Book 수량이 변경에 무반응)
고객A가 Book 10개를 주문하여 30개에서 -10만큼 차감되어 20개가 되었지만
고객B가 주문하기위해 조회한 Book 엔티티는 그것을 모르기때문에 여전히 잔여수량은
30개인상태로 -20만큼 차감되어 남은수량은 0개가 되어야하지만 10개가 남는상황 발생


해결방법 1 (JPA에서 제공하는 낙관적 LOCK : @Version)

@Version 은 Update가 실행될때마다 해당 컬럼에 +1을 실행하며 
조회시점에 Version 값과 업데이트시점에 Version 값이 다를경우
ObjectOptimisticLockingFailureException 에러를 발생시킨다.

따라서 2고객이 동시에 주문했을경우 
고객A가 주문시 조회한 상품 Version 값, 고객B가 주문시 조회한 상품 Version 값이
같더라도 고객A가 주문하면 DB Version 값이 +1 되므로 고객B가 주문시 
조회한 Version 값과 다르므로 ObjectOptimisticLockingFailureException 
에러가 발생하여 동시성 보장가능

해결방법 2 (비관적 LOCK : @Lock(LockModeType.PESSIMISTIC_WRITE))

LockMode 종류
LockModeType.PESSIMISTIC_WRITE
   일반적인 옵션. 데이터베이스에 쓰기 락
   다른 트랜잭션에서 읽기도 쓰기도 못함. (배타적 잠금)

LockModeType.PESSIMISTIC_READ
   반복 읽기만하고 수정하지 않는 용도로 락을 걸 때 사용
   다른 트랜잭션에서 읽기는 가능함. (공유 잠금)

LockModeType.PESSINISTIC_FORCE_INCREMENT
   Version 정보를 사용하는 비관적 락

1. Book 조회
고객A가 Book을 10개 주문 
   (Book 정보 조회시 LOCK 걸고 남은수량 30개인 Book 엔티티 리턴)
고객B가 Book을 20개 주문 
   (Book 정보 조회시 LOCK 에 의해 대기)

2. 주문 생성 (Book 수량 차감)
고객A가 Book 10개를 주문하여 수량 30개가 남은 Book 엔티티에 수량차감
메소드를 호출하여 30개에서 -10만큼 수량이 차감되며 변경감지에 의해 Update 실행

고객B가 고객A가 처리될때까지 대기했다가 처리완료되면 순차적으로 처리

3. 문제해결 (Book 수량이 변경에 순차적으로 처리)
고객A가 Book 10개를 주문하여 30개에서 -10만큼 차감되어 20개가 되고
고객A가 처리된 시점에 고객B가 상품데이터를 조회하기 때문에 순차적인 처리가가능