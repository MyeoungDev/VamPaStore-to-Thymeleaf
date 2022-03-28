package com.vam.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AttachMapperTests {

    @Autowired
    private AttachMapper attachMapper;

    @Test
    public void getAttachTest() {
        int bookId = 1021;

        System.out.println("이미지 정보: " + attachMapper.getAttachList(bookId));
    }
}
