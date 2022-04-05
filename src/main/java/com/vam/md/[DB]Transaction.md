# [Spring Boot] @Transactional 선언적 트랜잭션

먼저 @Transactional을 사용하기에 앞서 Transaction 에 대해 궁금한 사람은 전에 글에 정리해 놨으니 참고 바란다.

<br>

[[DB] Transaction 이란?](https://myeongdev.tistory.com/39)

<br>

Transaction 에 대한 설명과 사용 상황에 대한것은 위에 작성해놨으므로 이 글에서는 생략하도록 하겠다.

## @Transactional 이란?

데이터베이스 트랜잭션(Database Transaction)은 데이터베이스 관리 시스템 또는 유사한 시스템에서 상호작용의 단위이다. 여기서 유사한 시스템이란 트랜잭션이 성공과 실패가 분명하고 상호 독립적이며, 일관되고 믿을 수 있는 시스템을 의미한다.
[위키백과](https://ko.wikipedia.org/wiki/%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4_%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98)

이러한 트랜잭션은 ACID(Atomicity, Consistency, Isolation, Durability) 라는 특징을 갖게 된다.

#### Atomicity(원자성)
트랜잭션의 연산은 데이터베이스에 모두 반영되던지 아니면 모두 반영되지 않아야 한다.

#### Consistency(일관성)
트랜잭션이 그 실행을 성공적으로 완료하면 언제나 일관성 있는 데이터베이스 상태로 변환한다.
즉, 시스템이 가지고 있는 고정요소는 트랜잭션 수행 전과 후의 상태가 같아야 한다.

#### Isolation(독립성)
둘 이상의 트랜잭션이 동시에 실행되는 경우 어느 하나의 트랜잭션 연산에 다른 트랜잭션이 끼어들 수 없다.

#### Durability(영속성)
성공적으로 완료된 트랜잭션의 결과는 영구적으로 반영되어야 한다.


## Transaction 처리과정

![image](https://user-images.githubusercontent.com/73057935/161239821-7051785d-cbc8-4f6c-8ef9-a5cac4e8adb5.png)


<br>

위의 이미지는 트랜잭션의 처리과정이다.
트랜잭션의 처리에는 크게 세가지로 분류된다. Commit, Rollback 그리고 Savepoint이다.

#### - Commit: 하나의 트랜잭션이 성공적으로 끝나서 데이터베이스가 일관성있는 상태를 의미한다.
즉, 위의 그림처럼 모든 부분작업이 성공적으로 작업이 완료되면 그 때 한꺼번에 DB에 commit 된다.

#### - Rollback: 트랜잭션의 원자성이 깨질 때, 즉 하나의 처리가 비정상적으로 종료 되었을 때의 상태를 의미한다.
즉, 위의 그림처럼 어떠한 한 부분작업이 실패할 경우 rollback되어 트랜잭션이 행한 연산을 모두 취소한다.

이때, 모든 연산을 취소하지 않고 정해진 부분까지만 되돌리고 싶을 때 사옹하는 것이 Savepoint이다.

#### - Savepoint: 저장점을 정의하면 롤백할 때 트랜잭션에 포함된 전체를 롤백하는 것이 아니라, 현 시점에서 Savepoint 까지 트랜잭션의 일부만 롤백 할 수 있다.

### Transaction 5가지 상태

| 이름 | 설명 |
| --- | --- |
| 활성(Active) |  트랜잭션이 실행중인 상태 |
| 실패(Failed) | 트랜잭션 실행에 오류가 발생하여 중단된 상태 |
| 철회(Aborted) | 트랜잭션이 비정상적으로 종료되어 Rollback 연산을 수행한 상태 |
| 부분 완료(Partially Committed) | 트랜잭션의 마지막 연산까지 실행했지만, Commit 연산이 실행되기 직전의 상태
| 완료(Committed) | 트랜잭션이 성공적으로 종료되어 Commit 연산을 실행한 상태 |