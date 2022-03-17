package com.vam.service;

import com.vam.domain.AuthorVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorServiceTests {

    @Autowired
    AuthorService authorService;

    @Test
    public void authorEnrollTest() throws Exception {

        AuthorVO author = new AuthorVO();

        author.setNationId("01");
        author.setAuthorName("테스트");
        author.setAuthorIntro("테스트 소개");

        authorService.authorEnroll(author);
    }
}
