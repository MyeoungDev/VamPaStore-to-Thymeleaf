# [Spring Boot] @Transactional 선언적 트랜잭션

먼저 @Transactional을 사용하기에 앞서 Transaction 에 대해 궁금한 사람은 전에 글에 정리해 놨으니 참고 바란다.

<br>

[[DB] Transaction 이란?](https://myeongdev.tistory.com/39)

<br>

Transaction 에 대한 설명과 사용 상황에 대한것은 위에 작성해놨으므로 이 글에서는 생략하도록 하겠다.

## @Transactional

Spring에서 지원하는 선언적 Transaction 으로, xml 혹은 Configuration 을 통해 설정 할 수 있다.
Spring Boot 에서는 @Transactional 클래스 혹은 메서드 위에 해당 애노테이션을 선언하는 것으로 사용 할 수 있다.

하지만 수동으로 Configuration을 설정하고 싶은 분은 아래와 같이 설정 할 수 있다.
아래와 같이 수동으로 설정하는 경우는 DataSource와 DataSource와 관련된 설정들을 "수동" 으로 잡아줄 때 사용하면 된다.
그 케이스로는 멀티데이터소스, 라우팅데이터소스, MyBatis 자바설정 등등이 있다고 한다.

<br>

```java

    /* Transaction */
    @Bean
    public PlatformTransactionManager transactionManager() throws URISyntaxException, GeneralSecurityException {
        return new DataSourceTransactionManager(dataSource());
    }
```

<br>

그 외에 수동으로 잡는 케이스를 제외하고는 아래와 같이 Service 부분에 선언하여 사용하면 된다.

<br>

```java
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Transactional
@Service
public class AdminServiceImpl implements AdminService {
    ...
}
```

<br>

혹은 아래와 같이 사용하려는 메서드 위에 선언하는 것으로
해당 메서드만 Transaction 처리 할 수 있다.

```java

@Transactional
@Override
public int goodsModify(BookVO book) {
    logger.info("Service goodsModify");

    int result = adminMapper.goodsModify(book);

    if (result == 1 && book.getImageList() != null && book.getImageList().size() > 0) {

        adminMapper.deleteImgAll(book.getBookId());

        book.getImageList().forEach(attach ->{
            attach.setBookId(book.getBookId());
            adminMapper.imageEnroll(attach);
        });
    }

    return result;
}

```

## @Transactional 옵션

[공식 문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/transaction/annotation/Transactional.html)

<br>

해당 공식문서에 들어가면 옵션에 대한 자세한 내용들을 알 수 있다.

> propagenation: 트랜잭션 동작 도중 다른 트랜잭션을 호출 할 때, 어떻게 할 것인지 지정하는 옵션이다.
> 
> isolation: 트랜잭션에서 일관성 없는 데이터 허용 수준을 설정한다.
> 
> noRollbackFor=Exception.class: 특정 예외 발생 시 rollback 하지 않는다.
> 
> rollbackFor=Exception.class: 특정 예외 발생 시 rollback 한다.
> 
> timeout: 지정한 시간 내에 메서드 수행이 완료되지 않으면 rollback 한다.
> 
> readOnly: 트랜잭션을 읽기 전용으로 설정한다.

위와 같은 옵션들이 존재하고 공식문서 혹은 각 옵션에 대한 구글링을 하면 더욱 자세한 내용을 얻을 수 있을 것이다.


