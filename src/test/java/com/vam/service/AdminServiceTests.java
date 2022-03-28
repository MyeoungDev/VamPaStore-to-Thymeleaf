package com.vam.service;

import com.vam.domain.AttachImageVo;
import com.vam.domain.BookVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AdminServiceTests {

    @Autowired
    private AdminService adminService;
    @Test
    public void enrollTest() {

        BookVO book = new BookVO();
        // 상품 정보
        book.setBookName("service 테스트");
        book.setAuthorId(27);
        book.setPubleYear("2021-03-18");
        book.setPublisher("출판사");
        book.setCateCode("202001");
        book.setBookPrice(20000);
        book.setBookStock(300);
        book.setBookDiscount(0.23);
        book.setBookIntro("책 소개 ");
        book.setBookContents("책 목차 ");

        // 이미지 정보
        List<AttachImageVo> imageList = new ArrayList<AttachImageVo>();

        AttachImageVo image1 = new AttachImageVo();
        AttachImageVo image2 = new AttachImageVo();

        image1.setFileName("test Image 1");
        image1.setUploadPath("test image 1");
        image1.setUuid("test1111");

        image2.setFileName("test Image 2");
        image2.setUploadPath("test image 2");
        image2.setUuid("test2222");

        imageList.add(image1);
        imageList.add(image2);


        // bookEnroll() 메서드 호출
        adminService.bookEnroll(book);

        System.out.println("등록된 VO : " + book);
    }
}
