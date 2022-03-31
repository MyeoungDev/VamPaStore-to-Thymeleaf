package com.vam.mapper;

import com.vam.domain.AttachImageVo;
import com.vam.domain.BookVO;
import com.vam.domain.Criteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class AdminMapperTests {

    @Autowired
    private AdminMapper mapper;

    @Test
    public void bookEnrollTest() {
        BookVO book = new BookVO();

        book.setBookName("mapper 테스트");
        book.setAuthorId(123);
        book.setPubleYear("2021-03-18");
        book.setPublisher("출판사");
        book.setCateCode("0231");
        book.setBookPrice(20000);
        book.setBookStock(300);
        book.setBookDiscount(0.23);
        book.setBookIntro("책 소개 ");
        book.setBookContents("책 목차 ");

        mapper.bookEnroll(book);

    }

    /* 상품 리스트 & 상품 총 갯수 */
    @Test
    public void goodsGetListTest() {
        Criteria cri = new Criteria();

        /* 검색 조건 */
        cri.setKeyword("테스트");

        /* 검색 리스트 */
        List list = mapper.goodsGetList(cri);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("reuslt........." + list.get(i));
        }

        /* 상품 총 갯수 */
        int total = mapper.goodsGetTotal(cri);

        System.out.println("total result............."+ total);
    }

    @Test
    public void goodsGetDetail() {
        int bookId = 100;

        BookVO book = mapper.goodsGetDetail(bookId);
        System.out.println("detail Test result.........." + book);
    }

    @Test
    public void goodsModifyTest() {
        BookVO book = new BookVO();

        book.setBookId(1019);
        book.setBookName("수정 테스트1");
        book.setAuthorId(8160);
        book.setPubleYear("2022-03-18");
        book.setPublisher("출판사");
        book.setCateCode("103002");
        book.setBookPrice(20000);
        book.setBookStock(200);
        book.setBookDiscount(0.1);
        book.setBookIntro("책 소개 ");
        book.setBookContents("책 목차 ");

        mapper.goodsModify(book);

    }

    @Test
    public void goodsDeleteTest() {

        int bookId = 953;
        int result = mapper.goodsDelete(bookId);

        if (result == 1) {
            System.out.println("삭제 성공");
        } else {
            System.out.println("삭제 실패");
        } 
    }

    @Test
    public void imageEnrollTest() {
        AttachImageVo vo = new AttachImageVo();

        vo.setBookId(137);
        vo.setFileName("test");
        vo.setUuid("test");
        vo.setUploadPath("test");

        mapper.imageEnroll(vo);
    }

    @Test
    public void deleteImgAllTest() {
        int bookId = 1020;
        mapper.deleteImgAll(bookId);

    }

}

