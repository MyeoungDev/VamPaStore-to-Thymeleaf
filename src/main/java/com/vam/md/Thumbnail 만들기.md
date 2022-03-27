# [Spring Boot] Thumbnail 이미지 만들기 (Thumbnailator, Gradle)

[참고 블로그 VamPa](https://kimvampa.tistory.com/218?category=771727)

앞선 글에서 파일 업로드에 대해 작성하였다.

이미지 파일을 업로드시 미리 보여지는 이미지 즉, 썸네일은 필요하다.
그 이유는 하나의 페이지에 이미지가 한개라면 문제가 되지 않겠지만
만약 그 하나의 페이지에 수십만개의 이미지가 존재한다면 운영자의 입장에서는 많은 양의 트래픽으로 인해 서버 성능 저하, 많은 서버 비용이 발생 할 수 있고,
이용자의 입장에서는 긴 로딩 시간과 데이터 소모량이 증가하게 된다.
그렇기에 원본 이미지 파일보다 크기를 줄인 이미지를 보여주게 되면 이러한 문제가 해결 가능하다.


썸네일을 만드는 방법은 두가지로 BufferImage 된 이미지 객체를 Graphics2D 클래스를 이용하는 방법이고,
두번째 방법으로는 thumbnailator 라는 라이브러리를 사용하는 것이다.

전에 쓴 파일 업로드 Controller를 이용하여 작성할 것이다.
필요한 분은 아래 링크 참고 바란다.

[https://myeongdev.tistory.com/34](https://myeongdev.tistory.com/34)
[https://myeongdev.tistory.com/35](https://myeongdev.tistory.com/35)


> Graphics2D 이용
> 
> Thumbnailator 라이브러리 이용

### Graphics2D

업로드 파일을 DatePath에 맞는 폴더에 저장 할 것이고,
그 파일의 이름은 Thumbnail이라는 의미인 "s_" + uuid + originalFileName 으로 만들 것이다.

```java
@RequestMapping(value = "uploadTest", method = RequestMethod.POST)
public void uploadTestPOST(MultipartFile[] uploadFile) {
        logger.info("uploadTestPOST............");

        String uploadFolder = "C:\\upload";

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String formatDate = sdt.format(date);

        String datePath = formatDate.replace("-", File.separator);

        File uploadPath = new File(uploadFolder, datePath);

        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            
            String uploadFileName = multipartFile.getOriginalFilename();
            
            String uuid = UUID.randomUUID().toString();
            uploadFileName = uuid + "_" + uploadFileName;
            
            File saveFile = new File(uploadPath, uploadFileName);
        
            try {
                multipartFile.transferTo(saveFile);
            } catch (Exception e) {
                e.printStackTrace();
                }
            }
        }
    }
```

위의 Controller 코드는 DatePath 폴더에 uuid + originalFileName 으로 파일이름을 저장하는 것이다.

여기서 우리가 Graphics2D를 사용하여 Thumbnail 을 제작하기 위해서는 아래 코드가 필요하다.

```java

for (MultipartFile multipartFile : uploadFile) {

        String uploadFileName = multipartFile.getOriginalFilename();

        String uuid = UUID.randomUUID().toString();
        uploadFileName = uuid + "_" + uploadFileName;

        File saveFile = new File(uploadPath, uploadFileName);

        try {
            multipartFile.transferTo(saveFile);
            
            /* 추가된 부분............... */
            File thumbnailFile = new File(uploadPath, "s_" + uploadFileName)
            
            BufferedImage bo_image = ImageIo.read(saveFile);
            
            /* 비율 */
            double ratio = 3;
            int width = (int) (bo_image.getWidth() / ratio);
            int height = (int) (bo_image.getHeight() / ratio);
            
            // 생성자 매개변수 넓이, 높이, 생성될 이미지 타입
            BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            
            Graphics2D graphic = bt_image.createGraphics();
            
            graphic.drawImage(bo_image, 0, 0, width, height, null);
            
            ImageIO.write(bt_image, "jpg", thumbnailFile);
            /* ...................... */
            
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
```

위의 코드에서 ImageIo 클래스는 이미지를 읽거나 생성할 수 있고, BufferdImage는 이미지 처리와 조작에 대한 메서드를 제공해준다.
Graphics2D는 흰 도화지에 이미지를 그려넣을 수 있는(약간 그림판?) 클래스라고 할 수 있다.

위의 코드는 원본 파일을 원하는 비율에 맞게 조정하여 새로운 이미지 파일을 만든 후
그 이미지 파일을 thumbnailFile에 저장하는 형식이다.
처음보는 클래스가 많아 처음에는 뭐지 했지만 약간 대학교때 파이썬으로 거북이기 그리기 같은 느낌이라 금방 익숙해 졌다.


### Thumbnailator (Gradle)

#### Gradle 라이브러리 추가
```
implementation group: 'net.coobird', name: 'thumbnailator', version: '0.4.1'	/* thumbnailator */
```

```java
File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);

        BufferedImage bo_img = ImageIO.read(saveFile);
        double ratio = 3;
        int width = (int) (bo_img.getWidth() / ratio);
        int height = (int) (bo_img.getHeight() / ratio);

        Thumbnails.of(saveFile)
                .size(width, height)
                .toFile(thumbnailFile);
```

위와 같이 작성하면 Thumbnailator 라이브러리를 이용하여 썸네일을 더욱 쉽게 만들 수 있다.
