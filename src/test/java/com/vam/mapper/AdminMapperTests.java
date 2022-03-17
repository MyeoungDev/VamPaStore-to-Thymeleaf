package com.vam.mapper;

import com.vam.domain.BookVO;
import com.vam.domain.Criteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
