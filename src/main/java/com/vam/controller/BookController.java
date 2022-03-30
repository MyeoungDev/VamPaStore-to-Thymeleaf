package com.vam.controller;

import com.vam.domain.AttachImageVo;
import com.vam.service.AttachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private AttachService attachService;

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public void mainPageGET() {
        logger.info("메인 페이지 진입");
    }

    @GetMapping(value = "/display")
    public ResponseEntity<byte[]> getImage(String fileName) {
        logger.info("getImage........" + fileName);

        File file = new File("C:\\upload\\" + fileName);

        ResponseEntity<byte[]> result = null;
        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping(value = "/getAttachList")
    public ResponseEntity<List<AttachImageVo>> getAttachList(int bookId) {
        logger.info("getAttachList..............." + bookId);

        return new ResponseEntity<List<AttachImageVo>>(attachService.getAttachList(bookId), HttpStatus.OK);
    }
}
