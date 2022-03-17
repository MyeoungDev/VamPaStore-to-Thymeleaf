# [Spring Boot] Mybatis, Thymeleaf Paging 구현하기 (Mybatis, MySql, Thymeleaf)

블로그를 하면서 하나 느낀게 공부할 때 그냥 이해 됐다고 넘어가면 오래 안가고
글로 작성하면서 다시한번 이해해야 더 오래가는 것 같다.
그렇기에 조금 애먹었던 Mybatis와 Thymeleaf를 이용한 Paging처리를 글로 남기려 한다.

<br>

현재 내가 하고있는거는 책 쇼핑몰이다.
<br>

[Kim VamPa님의 블로그](https://kimvampa.tistory.com/188)

아직까지는 혼자 프로젝트를 진행해보려해도 백지에서 시작하는게 너무 막막하고 힘들다.
그래서 위의 블로그분의 글을 따라가며 방향성을 잡으면서 진행중이다.
항상 양질의 개발 글을 공유해주고 제공해주시는 모든 개발자분들에게 감사를 표한다.

[Paging 참고 블로그 (도뎡이님의 블로그)](https://congsong.tistory.com/26)


본론으로 들어가기 전 처음 스스로 해본 Paging이고 코드가 더럽거나 복잡할 수 있다.
이해 바라면서 시작해보겠다.

## 1. domain 폴더에 Criteria 클래스 작성

```java
package com.vam.domain;

import lombok.Data;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class Criteria {

    /* 현재 페이지 번호 */
    private int pageNum;

    /* 페이지 표시 개수 */
    private int amount;

    /* 페이지 skip */
    private int skip;

    /* 검색 타입 */
    private String type;

    /* 검색 키워드 */
    private String keyword;

    /* Criteria 생성자 */
    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
        this.skip = (pageNum - 1) * amount;
    }

    /* Criteria 기본 생성자 */
    public Criteria() {
        this(1, 10);
    }

    /* 검색 타입 데이터 배열 변환 */
    public String[] getTypeArr() {
        return type == null ? new String[]{} : type.split("");
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        this.skip = (pageNum - 1) * this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.skip = (this.pageNum - 1) * amount;
    }

    public String makeQueryString(int pageNum) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                /* pageNum 이랑 searchType and keyword 이거 3개만 설정 하면 나머지는 건들지 않아도 된다? */
                // 현재 페이지 번호
                .queryParam("pageNum", pageNum)
                // 페이지 표시 개수 -> 어차피 10개로 고정
//                .queryParam("amount", amount)
                .queryParam("searchType", type)
                .queryParam("keyword", keyword)
                .build()
                .encode();

        return uriComponents.toUriString();
    }

}
```

위으 Criteria class에는 기본적인 paging에 대한 요소를 작성하고
그 이후 View에서 page 전환을 위해 makeQueryString을 작성했다.
가장 애를 먹었던 부분이 View에서 어떻게 하면 말끔하게 이동시킬 수 있을까였다.

Controller를 통해 @RequestParam 을 통해 구현하는 방법도 있다.

하지만 나는 위의 도뎡이님의 블로그 방식처럼 makeQueryString으로 URI를 합쳐서 하는 방식으로 만들었다.

## 2. domain 폴더에 PageDTO 작성

```java
package com.vam.domain;

import lombok.Data;

@Data
public class PageDTO {

    /* 페이지 시작 번호 */
    private int pageStart;

    /* 페이지 끝 번호 */
    private int pageEnd;

    /* 이전, 다음 버튼 존재 유무 */
    private boolean next, prev;

    /* 행 전체 개수 */
    private int total;

    /* 현재페이지 번호(pageNum), 행 표시 수(amount), 검색 키워드(keyword), 검색 종류(type)*/
    private Criteria cri;

    /* 생성자(클래스 호출 시 각 변수 값 초기화 */
    public PageDTO(Criteria cri, int total) {

        /* cri, total 초기화 */
        this.cri = cri;
        this.total = total;

        /* 페이지 끝 번호 */
        this.pageEnd = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;

        /* 페이지 시작 번호 */
        this.pageStart = this.pageEnd - 9;

        /* 전체 마지막 페이지 번호 */
        int realEnd = (int) (Math.ceil(total * 1.0 / cri.getAmount()));

        /* 페이지 끝 번호 유효성 체크 */
        if (realEnd < pageEnd) {
            this.pageEnd = realEnd;
        }

        /* 이전 버튼 값 초기화 */
        this.prev = this.pageStart > 1;

        /* 다음 버튼 값 초기화 */
        this.next = this.pageEnd < realEnd;
    }

}
```
PageDTO 에서는 페이징에 필요한 계산을 생성자를 통해 주입되도록 만들고
Criteria를 갖고와 쓸 수 있도록 하였다.

## 3. Mapper 작성

```java
package com.vam.mapper;

import com.vam.domain.AuthorVO;
import com.vam.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthorMapper {

    /* 작가 등록 */
    public void authorEnroll(AuthorVO author);

    /* 작가 목록 */
    public List<AuthorVO> authorGetList(Criteria cri);

    /* 작가 총 수 */
    public int authorGetTotal(Criteria cri);

}
```

```mysql
<select id="authorGetList" resultType="com.vam.domain.AuthorVO">
        SELECT
            authorid, authorname, nationid, regdate, updatedate
        FROM
            vam_author
        <if test="keyword != null">
            WHERE
                authorname
            LIKE
                concat('%', #{keyword}, '%')
        </if>
        ORDER BY
            authorid
        DESC
        LIMIT #{skip}, #{amount}
    </select>

    <select id="authorGetTotal" resultType="int">
        SELECT
            count(*)
        FROM
            vam_author
        <if test="keyword != null">
            WHERE
                authorname
            LIKE
                concat('%', #{keyword}, '%')
        </if>
    </select>
```

## 4. Service 작성

Service Interface
```java
    /* 작가 목록 */
    public List<AuthorVO> authorGetList(Criteria cri) throws Exception;

    /* 작가 총 수 */
    public int authorGetTotal(Criteria cri) throws Exception;
```

Service Implements
```java
    @Override
    public List<AuthorVO> authorGetList(Criteria cri) throws Exception {
        logger.info("(service)authorGetList()........... " + cri);
        return authorMapper.authorGetList(cri);
    }

    @Override
    public int authorGetTotal(Criteria cri) throws Exception {
        logger.info("service authorGetTotal");
        return authorMapper.authorGetTotal(cri);
    }
```

5. Controller 작성

```java
/* 작가 관리 페이지 접속 */
@RequestMapping(value = "authorManage", method = RequestMethod.GET)
public void authorManageGET(Criteria cri, Model model) throws Exception {

    logger.info("작가 관리 페이지 접속 ..........." + cri);

    List<AuthorVO> list = authorService.authorGetList(cri);

    model.addAttribute("list", list);

    /* 페이지 이동 인터페이스 데이터 */
    int total = authorService.authorGetTotal(cri);

    PageDTO pageMaker = new PageDTO(cri, total);

    model.addAttribute("pageMaker", pageMaker);

}
```

## 6. View 구현 (Thymeleaf)

```html
<!-- 이전 버튼 -->
<th:block th:if="${pageMaker.prev}">
    <li class="pageMaker_btn prev">
        <a href="javascript:void(0)" th:onclick="movePage([[ ${#request.requestURI} ]], [[ ${pageMaker.cri.makeQueryString(pageMaker.pageStart - 1)} ]])">이전</a>
    </li>
</th:block>

<!-- 페이지 번호 -->
<th:block th:with="pageMaker = ${pageMaker}">
    <th:block th:each="num : *{#numbers.sequence(pageMaker.pageStart, pageMaker.pageEnd)}">
        <li class="pageMaker_btn" th:classappend="${pageMaker.cri.pageNum == num} ? 'active' : ''">
            <a href="javascript:void(0)" th:text="${num}" th:onclick="movePage([[ ${#request.requestURI} ]],[[ ${pageMaker.cri.makeQueryString(num)} ]])"></a>
        </li>
    </th:block>
</th:block>


<!-- 다음 버튼 -->
<th:block th:if="${pageMaker.next}">
    <li class="pageMaker_btn next">
        <a href="javascript:void(0)" th:onclick="movePage( [[ ${#request.requestURI} ]], [[ ${pageMaker.cri.makeQueryString(pageMaker.pageEnd + 1)} ]])">다음</a>
    </li>
</th:block>
```

```javascript
<script th:javascript>
/* ![CDATA[ */
function movePage(uri, queryString) {
    console.log(uri);
    console.log(queryString);

    location.href = uri + queryString;
}
/* ]]*/
</script>

```

Criteria 클래스에서 makeQueryString 을 이용하여 queryString을 만들고 JavaScript로 location.href로 요청 RequestUri 와 queryString을 합쳐
페이징하는 방법을 사용하였다.

이렇게 하면 Spring Boot 에서 Mybatis와 MySql, Thymeleaf를 이용하 페이징 처리가 끝이 난다.

위에 작성한 도뎡이님 방식을 사용한 것이고,
Thymeleaf 문법에 대해 궁금한게 몇가지 있어 질문으로 댓글을 남겨두었다.
나의 구글링으로는 궁금증을 해결하지 못하였다,,,
만약 도뎡이님이 답변해주신다면 여기에 내가 궁금했던 부분과 그에 대한 답글을 남겨놓도록 해야겠다.

### 궁금증
#### 나의 질문
안녕하세요 도뎡이님 양질의 지식공유 글 정말 감사합니다! 다름이 아니라 공부하며 도뎡이님의 글에 도움을 받던중에 궁금하던게 생겨서 이렇게 댓글로 질문 남겨봅니다...

th:each="pageNo : *{#numbers.sequence( firstPage, lastPage )}"
여기서 먼저 궁금한거 ${#numbers.sequence} 와 위의 *{#numbers.sequence}에 대한 차이가 정말 너무 궁금합니다...
그리고 보통 #numbers.sequence(${firstpage}, ${lastPage}) 이런식으로 하게 된다면 오류가 나게 되던데
th:with 혹은 th:object로 지역변수로 할당 받아 하게 되면 도뎡이님 방식으로 진행이 가던데 이유를 알 수 있을까요? 처음 질문인 ${} 이것과 *{} 이것의 차이 일까요?

th:onclick="movePage([[ ${#request.requestURI} ]], [[ ${params.makeQueryString(pageNo)} ]])"

이 코드에서 movePage의 자바스크립트 함수에 대괄호 [[ ]]의 작성이유가 궁급합니다....
제가 도뎡이님의 글을 놓친걸수도 있겠지만 잘 이해가 되질 않습니다...
그리고 구글링으로 알아본 결과로 reqeustURI를 받아오기 위해 Thymeleaf에서는 ${#httpServletRequest} 를 다른분들이 많이 사용하시는 것 같았습니다.
혹시 ${#reqeust} 와 ${#httpServletRequest}의 차이는 무엇인지 알 수 있을까요?

궁금증을 갖고 계속 구글링 해보았지만 답을 얻지 못해서 이렇게 댓글 남깁니다. 귀찮게 해드려서 죄송합니다.

### 도뎡이님 답변

안녕하세요 ^^~

1. ${ } 표현식과 *{ } 표현식은 공통적으로 객체에 접근할 수 있다는 것인데요. *{ } 표현식은 컨트롤러에서 전달받은 객체를 th:object로 선언한 경우에만 사용이 가능합니다.

2. 대괄호를 작성하는 이유는, 해당 함수의 인자로 requestURI와 makeQueryString(pageNo) 메서드의 리턴 값을 전달해주기 위함입니다. movePage( ) 함수를 개발자 도구를 이용해서 디버깅해 보시거나 콘솔로 찍어보시면, 함수 파라미터로 전달받은 값을 확인해 보실 수 있으세요!

3. request와 httpServletRequest는 동일하다고 보시면 됩니다 :)

충분히 이해하실 정도의 답변이 되었을지 모르겠네요 ^^...

---

꽤 오래 걸리던 Paging처리를 이렇게 구현해봤다.
완전히 내껄로 만드려면 더 공부해야될것같다.

이상 끝.