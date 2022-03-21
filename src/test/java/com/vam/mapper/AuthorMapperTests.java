package com.vam.mapper;

import com.vam.domain.AuthorVO;
import com.vam.domain.Criteria;
import com.vam.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AuthorMapperTests {

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private AuthorService authorService;

    /* 작가 등록 테스트 */
    @Test
    public void authorEnroll() throws Exception {
        AuthorVO author = new AuthorVO();

        author.setNationId("01");
        author.setAuthorName("테스트");
        author.setAuthorIntro("테스트 소개");

        authorMapper.authorEnroll(author);
    }

    /* 작가 리스트 Get Test*/
    @Test
    public void authorGetListTest() throws Exception {
        Criteria cri = new Criteria(1, 10);
        cri.setKeyword("김난도");
        List<AuthorVO> list = authorMapper.authorGetList(cri);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("list" + i + ".........." + list.get(i));
        }
    }

    /* 작가 총 수 */
    @Test
    public void authorGetTotal() throws Exception {
        Criteria cri = new Criteria();

        cri.setKeyword("김난도");
        int total = authorMapper.authorGetTotal(cri);

        System.out.println("total.........." + total);
    }

    @Test
    public void authorGetDetail() throws Exception {

        int authorId = 20;

        System.out.println("author ........" + authorService.authorGetDetail(authorId));

    }

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

    @Test
    public void authorDeleteTest() {

        int authorId = 8156;
        int result = authorMapper.authorDelete(authorId);

        if (result == 1) {
            System.out.println("작가 삭제 성공");
        } else {
            System.out.println("작가 삭제 실패");
        }

    }






}
