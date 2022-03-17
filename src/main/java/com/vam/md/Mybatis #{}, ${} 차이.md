#Mybatis #{}, ${} 차이

공부하던 중 Thymeleaf를 사용하다 보니 무의식 적으로
xml 파일에서 ${} 이런 표현식을 사용하게 되었다.
처음에 위화감을 느끼지 못하고 Junit으로 단위 테스트를 진행했다.

역시나 당연하게 에러가 나게 되고 한참을 해메게 되다가 찾게 되었다.

내가 사용한 xml

```mysql
<update id="authorModify">
    UPDATE
        vam_author
    SET
        authorName = ${authorName},
        nationId = ${nationId},
        authorIntro = ${authorIntro},
        updateDate = now()
    WHERE
        authorId = ${authorId}
</update>
```

단위 테스트
```java
@Test
public void authorModifyTest() throws  Exception {
    AuthorVO test = new AuthorVO();

    test.setAuthorId(1);
    System.out.println("수정 전........" + authorMapper.authorGetDetail(test.getAuthorId()));

    test.setAuthorName("수정");
    test.setNationId("01");
    test.setAuthorIntro("수정");

    authorMapper.authorModify(test);

    System.out.println("수정 후.........." + authorMapper.authorGetDetail(test.getAuthorId()));
}
```

이런식으로 진행을 하였고 에러는
```
### Error updating database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column '수정' in 'field list'
```
라는 에러를 받게 되었다. sql 구문과 DB 테이블에 오류는 없다고 판단하고 코드를 유심히 보다가 xml 코드를 잘못 작성하였다는 것을 발견했다.

```${}``` 이러한 jstl에서 사용하는 표현식을 사용해서 오류가 나타나게 된것이었다.

```mysql
<update id="authorModify">
    UPDATE
        vam_author
    SET
        authorName = #{authorName},
        nationId = #{nationId},
        authorIntro = #{authorIntro},
        updateDate = now()
    WHERE
        authorId = #{authorId}
</update>
```
위의 코드처럼 ```#{}``` 의 표현식을 사용하면 이상없이 진행된다.
하지만 두개의 차이가 무엇인지 궁금해져서 알아보았다.

두개의 차이에는 PreparedStatement 와 Statement 의 차이가 존재한다.

### #{}

1. `#{}` 사용시 PreparedStatement 생성
2. PreparedStatement 물음표(?)에 안전하게 바인딩
3. 들어오는 데이터를 문자열로 인식하기 때문에 자동으로 따음표가 붙게 된다.

안전하고 빠르기 때문에 선호, 대표적으로 사용

### ${}

1. ${} 사용시 Statement 생성
2. Statement 매개변수 값 '그대로' 전달, 즉 따음표가 붙지 않게된다.

이러한 두개의 차이로 인해 #{} 을 사용하는것을 권장하고 그렇게 많이 사용하고 있다고 한다.
그리고 가장 큰 차이점으로는 ${}은 SQL Injection 같은 문제에 대해 취약하다고 한다.

<br>
SQL 삽입(영어: SQL Injection, SQL 인젝션, SQL 주입)은 응용 프로그램 보안 상의 허점을 의도적으로 이용해, 악의적인 SQL문을 실행되게 함으로써 데이터베이스를 비정상적으로 조작하는 코드 인젝션 공격 방법이다.
-위키백과-

[참고 블로그](https://java119.tistory.com/39)