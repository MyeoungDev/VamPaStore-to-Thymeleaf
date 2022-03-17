# 위지윅이란? CK Editor 사용하기

[https://ckeditor.com/](https://ckeditor.com/)

## 위지윅 이란?

위지위그(WYSIWYG: What You See Is What You Get, "보는 대로 얻는다")는 문서 편집 과정에서 화면에 포맷된 낱말, 문장이 출력물과 동일하게 나오는 방식을 말한다. 이는 편집 명령어를 입력하여 글꼴이나 문장 형태를 바꾸는 방식과 구별된다.
-위키백과

즉, 우리가 흔히 게시판에서 보는 텍스트 에디터를 뜻한다.
이것을 라이브러리를 갖고와 편하게 사용할 수 있다.
위지윅 라이브러로는 "CK Editor", "TinyMCE", "Toast Editor", "Summernote" 등이 있다.


### CDN 방식으로 CK Editor 사용

사용할 html 파일에 아래 스크립트 태그를 추가해준다.

```html
<script src="https://cdn.ckeditor.com/ckeditor5/32.0.0/classic/ckeditor.js"></script>
```

그 이후 `<textarea>` 코드를 Javascript 코드를 통해 CK editor을 적용해 준다.

```html
<textarea id="textarea_ck_test"></textarea>
```

```javascript
ClassicEditor
    .create(document.querySelector('textarea_ck_test'))
    .catch(error => {
        console.error(error);
    });
```

그 이후 아래와 같은 텍스트 에디터를 손쉽게 구현이 가능하다.

![image](https://user-images.githubusercontent.com/73057935/156571670-f4190f6f-4863-457e-90e1-9529089be4b8.png)

위의 사진과 같이 Heading1 이라는 효과를 주었을때에 DB에 `<h1>위지윅 테스트</h1>` 로 저장되게 된다.

[https://ckeditor.com/docs/ckeditor5/latest/](https://ckeditor.com/docs/ckeditor5/latest/)

위의 documentation을 통해 커스터 마이징을 하여 사용도 가능하고, 각종 유저분들이 커스터마이징한것도 구글에서 찾아 볼 수있다.

[https://opentutorials.org/course/2473/13864](https://opentutorials.org/course/2473/13864)

또한 생활코딩의 이고잉님이 위지윅과 CK editor에 대해 설명해주신 것도 있으니 보면 좋을꺼 같다.



