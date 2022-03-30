# [Mybatis] 생성한 키(PK) 리턴받기 두가지 방법(selectKey, useGeneratedKeys) 

한 테이블에 insert 할 때 그 PK 값을 Foreign Key로 다른 테이블을 매핑시켜 등록 시키고 싶은 경우가 있다.
예를 들면, 현재 나의 상황처럼 어떠한 상품에 이미지를 등록하는데 그 상품의 PK를 FK로 받아 동시에 DB에 넘겨주고 싶었다.
즉, 그렇게 하려면 생성된 PK를 바로 리턴받아 사용해야 했고 그렇기에 Mybatis의 selectKey, useGeneratedKeys 두 가지 방법을 알게 되었다.
현재 사용하고 있는 DB는 MySql이다.

> selectKey
> 
> useGenratedKeys

## selecyKey

[selectKey 공식문서](https://mybatis.org/mybatis-3/sqlmap-xml.html)

<br>

> MyBatis has another way to deal with key generation for databases that don't support auto-generated column types, or perhaps don't yet support the JDBC driver support for auto-generated keys.
> 
> Here's a simple (silly) example that would generate a random ID (something you'd likely never do, but this demonstrates the flexibility and how MyBatis really doesn't mind):

해석하면 Mybatis에는 자동 생성된 컬럼, 키에 대한 JDBC Driver가 처리하지 못하는경우를 처리하는 또 다른 방식이 존재한다.
밑에 줄은 랜덤 ID를 생성하는 멍청한 예시일 뿐 절대 저렇게 하지말라고 한다. 좀 유쾌하네...

그 예시를 한번 봐보자

<br>

```xml

<insert id="insertAuthor">
    <selectKey keyProperty="id" resultType="int" order="BEFORE">
        select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
    </selectKey>
    insert into Author
    (id, username, password, email,bio, favourite_section)
    values
    (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
</insert>
```

<br>

위와 같은 방식은 해석에 나온대로 생성된 키를 return 받아 우리가 다른 query문으로 사용이 가능하다.
예를 들어 한 게시판(board)에 이미지를 첨부하고싶고, 이미지 테이블에는 boardId FK로 받아 등록하고싶을 것이다.
그럴 때 위와 같은 방법으로 `<selectKey>`를 사용하여 board 등록된 키를 바로 가져와서 사용 가능하다.

- keyProperty 속성에는 반환받고 싶은 column
- resultType은 반환 타입
- order은 쿼리 실행 전에 반환(before) 혹은 쿼리 실행 후 반환(after)을 정한다.

위와 같이 반환받는 다른 방식으로는 useGeneratedKey 가 있다.

## useGeneratedKeys

> if your database supports auto-generated key fields (e.g. MySQL and SQL Server),
> 
> then you can simply set useGeneratedKeys="true" and set the keyProperty to the target property and you're done.
> 
> For example, if the Author table above had used an auto-generated column type for the id, the statement would be modified as follows:

위의 내용을 해석하면, 만약 너의 데이터베이스가 자동 생성 키(MySql의 auto increased) 지원한다면
그땐 너는 간단히 useGeneratedKeys 를 설정하여 그 타겟 속성을 너가 사용할 수 있다.
예를들어, 만약 작가 테이블의 Id를 자동생성을 사한다면 다음과 같이 사용할 수 있다.

공식문서의 예시 코드를 확인해보자

<br>

```xml

<insert id="insertAuthor" useGeneratedKeys="true"
    keyProperty="id">
  insert into Author (username,password,email,bio)
  values (#{username},#{password},#{email},#{bio})
</insert>

```

<br>

위의 코드에는 `<insert></insert>` 라는 Mybatis 쿼리 문 안에
useGeneratedKeys="true" 와 keyProperty="id" 라는 속성이 쓰여 있다.
각 속성의 의미에 대해 알아보자.

- useGeneratedKeys: 자동 생성된 키를 반환 받을 것인가. (default = false)
- keyProperty: 어떤 column 을 반환 받을 것인지. 즉, key의 coulmn명을 작성하면 된다.

이렇게 간단하게 Mybatis에서 생성된 키를 반환받아 또 다른 쿼리문으로 실행이 가능하다.

마지막으로 selectKey 속성에 대해 알아보고 마무리 하겠다.

### selectKey Attributes

| Attributes | Description |
| --- | --- |
| keyProperty | selectKey 구문의 결과가 셋팅될 대상 프로퍼티
| resultType | 결과의 타입. MyBatis는 이 기능을 제거할 수 있지만 추가하는게 문제가 되지는 않을것이다. MyBatis는 String을 포함하여 키로 사용될 수 있는 간단한 타입을 허용한다.
| order | BEFORE 또는 AFTER을 셋팅할 수 있다. BEFORE 로 셋팅하면, 키를 먼저 조회하고 그 값을 keyProperty에 셋팅한 뒤 insert 구문을 실행한다. AFTER로 셋팅하면, insert 구문을 실행한 뒤 selectKey 구문을 실행한다. Oracle과 같은 데이터베이스에서는 insert 구문 내부에서 일관된 호출 형태로 처리한다.
| statementType | 위 내용과 같다. MyBatis는 Statement, PreparedStatement 그리고 CallableStatement을 매핑하기 위해 STATEMENT, PREPARED 그리고 CALLABLE 구문 타입을 지원한다.

이상으로 글을 마치겠습니다.


