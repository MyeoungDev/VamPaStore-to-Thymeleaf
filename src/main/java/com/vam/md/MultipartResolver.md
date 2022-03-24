# [Spring boot] MultipartResolver 이미지 파일 업로드

[참고 블로그 VamPa](https://kimvampa.tistory.com/211?category=771727)

### gradle 라이브러리 추가

Apache Commons-Io 와 Apache Commons Fileupload 라이브러리 두개를 추가해 준다.

Apache Commons Fileupload 라이브러리 추가 안했다가 에러나서 혼자 삽질했다...

[MultipartResolver Bean Creation Error 해결](https://myeongdev.tistory.com/33)

```
implementation 'commons-io:commons-io:2.11.0'	/* Apache commons-io */
implementation group: 'commons-fileupload', name: 'commons-fileupload', version: '1.4' /* Apache Commons FileUpload */
```

### Configuration 설정 Bean 등록

```java
@Bean
public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩 설정
    multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 파일당 업로드 크기 제한 (5MB)
    return multipartResolver;
}
```

### View에서 JavaScript 동작

```html
<div class="form_section">
    <div class="form_section_title">
        <label>상품 이미지</label>
    </div>
    <div class="form_section_content">
        <input type="file" name="uploadFile">
    </div>
</div>  
```

```javascript
document.querySelector("input[type=file]").addEventListener("change", function () {
    let fileInput = document.querySelector("input[name=uploadFile]");
    let fileObj = fileInput.files[0];

    let formData = new FormData();

    formData.append("uploadFile", fileObj);

    console.log("fileList: " + fileList);
    console.log("fileList[0].name" + fileList[0].name);
    console.log("fileList[0].size" + fileList[0].size);
    console.log("fileList[0].type" + fileList[0].type);
    
    $.ajax({
        url: '/uploadTest',
        processData : false,
        contentType : false,
        data : formData,
        type : 'POST',
        dataType : 'json'
    });
});
```

```<input type="file">``` 태그는 javascript에서 위와 같이 사용가능 하다. 

FormData는 쉽게 말하면 가상의 form태그라고 볼 수있다고 한다.
첨부파일을 서버로 전송하기 위한 객체이다.

위의 ajax 통신에서 processData, contentType은 'false'를 해주어야 서버로 전송된다.
> url: 서버로 요청을 보낼 url
> 
> processData: 서버로 전송할 데이터를 queryString으로 변환 여부
> 
> contentType: 서버로 전송되는 content-type
> 
> data: 서버로 전송할 data
> 
> type: 서버 요청 타입
> 
> dataType: 서버로부터 반환받을 데이터 타입

### Controller

```java
@RequestMapping(value = "uploadTest", method = RequestMethod.POST)
public void uploadTestPOST(MultipartFile[] uploadFile) {
        logger.info("uploadTestPOST............");
        
        // 내가 업로드 파일을 저장할 경로
        String uploadFolder = "C:\\upload";
        for (MultipartFile multipartFile : uploadFile) {
            String uploadFileName = multipartFile.getOriginalFilename();
            // 저장할 파일, 생성자로 경로와 이름을 지정해줌.
            File saveFile = new File(uploadFolder, uploadFileName);

            try {
                multipartFile.transferTo(saveFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
```
[MultipartFile 공식 문서](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/multipart/MultipartFile.html)

MultipartFile
- View에서 전송한 multipart 타입의 파일을 다룰 수 있도록 해주는 인터페이스
- 파일의 이름 변환, 사이즈 변환, 특정 경로에 저장 등을 수행 가능

이후 실행시키면

![image](https://user-images.githubusercontent.com/73057935/159911369-00079cad-0dee-4e51-955d-c049a599efa4.png)

Success!!

성공적으로 내가 업로드한 파일이 내가 지정한 폴더에 저장된다.

공부 목적으로 최대한 간결하고 자세하게 설명해서 글 작성 함. 

또한 나같은 초보자도 쉽게 사용 가능하게 끔 

