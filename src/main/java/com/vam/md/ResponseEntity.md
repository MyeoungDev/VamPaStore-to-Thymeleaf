# [Spring Boot] HttpEntity, ResponseEntity 란?

공부를 하던 중 ResponseEntity 를 사용하게 되었다.
그런데 Controller에서 View에 정보를 전송할 때 Http status, header를 조작 할 수 있는 메서드를 제공한다고만 알고 사용했다.
하지만 어떠한 구조로 이루어져 있는지 궁금하기도 해서 ResponseEntity 상위 클래스에 HttpEntity 라는 클래스가 있다는 것을 알게 되었고,
두 클래스 역할과 쓰임세를 알아보려 하였다.

## HttpEntity

[HttpEntity 공식문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html)


>public class HttpEntity<T>
>
>extends Object
>
>Represents an HTTP request or response entity, consisting of headers and body.
>
>Often used in combination with the RestTemplate, like so:

공식 문서에는 위와 같이 나와있다.
해석을 하면 헤더와 바디로 이루어진 HTTP request, response 엔티티를 나타낸다. 라고 나와있다.

예제 코드를 보면

<br>

```java
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.TEXT_PLAIN);
HttpEntity<String> entity = new HttpEntity<>("Hello World", headers);
URI location = template.postForLocation("https://example.com", entity);
```

<br>

HttpHeaders 라는 객체를 생성하여 Header의 ContentType을 설정하여 요청을 보낼 수 있는걸로 보인다.

그 아래 Spring Boot의 Controller 사용 예제 코드도 나와 있다.

<br>

```java
@GetMapping("/handle")
public HttpEntity<String> handle(){
    HttpHeaders responseHeaders=new HttpHeaders();
    responseHeaders.set("MyResponseHeader","MyValue");
    return new HttpEntity<>("Hello World",responseHeaders);
}
```

<br>

위의 코드를 보면 View에서 @GetMapping 으로 인한 요청시 Http Header를 설정하여 반환 할 수 있는걸로 보인다.
이 외에도 공식문서를 보면 getBody(), getHeader() 등의 body와 header를 반환받을 수 있고,
MultiValueMap<>을 사용하여 Header 와 Body를 매핑 하여 요청, 응답 할 수 있는걸로 보인다.

## ResponseEntity

[ResponseEntity 공식문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html)

>public class ResponseEntity<T>
>
>extends HttpEntity<T>
>
>Extension of HttpEntity that adds an HttpStatus status code. Used in RestTemplate as well as in @Controller methods.
>
>In RestTemplate, this class is returned by getForEntity() and exchange():

공식 문서에는 위와 같이 나와 있으며,
해석해보자면, HttpEntity 의 확정이며, HttpStatus 의 상태코드를 더할 수 있으며, @Controller에서 더욱 잘 사용할 수 있다고 나와있는 것 같다.

이제 예제코드를 한번 봐보겠다.

<br>

```java
@RequestMapping("/handle")
public ResponseEntity<String> handle() {
    URI location = ...;
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setLocation(location);
    responseHeaders.set("MyResponseHeader", "MyValue");
    return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
}

```

<br>

위의 예제코드가 내가 사용했던 코드와 비슷한 형식이다.
반환 타입을 ResponseEntity<T> 의 형식으로 하고 반환시 HttpStatus Code를 반환해주는 형식이다.

```java
@RequestMapping(value = "/test")
public ResponseEntity<List<TestVO>>testController(){
        ...
        TestVo list=new ArrayList<>();
        ...
        return ResponseEntity<List<TestVo>>result=new ResponseEntity<List<TestVo>>(list,HttpStatus.OK);
        }
```

위와 같은 형식의 코드를 사용하여 ResponseEntity를 통해 List 타입의 값을 View에 넘겨주며,
또한 상태코드까지 같이 반환시키도록 하였다.

즉, HttpEntity 와 ResponseEntity 의 차이는 HttpStatus StatusCode 의 반환 여부라고 볼 수 있을것 같다.
ResponseEntity 의 공식문서에 다양한 Constructor 방식과 메서드들이 존재하므로 사용하기 전에 한번 둘러보는게 좋을 것 같다.
