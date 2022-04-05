package com.vam.mapper;

import com.vam.domain.AttachImageVo;
import com.vam.domain.BookVO;
import com.vam.domain.Criteria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Test
    public void checkImageListTest() {
        List<AttachImageVo> fileList = mapper.checkFileList();

        List<Path> checkFilePath = new ArrayList<>();


        fileList.forEach(vo -> {
            Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
            checkFilePath.add(path);
        });

        System.out.println("fileCheckPath.............");
        checkFilePath.forEach(list -> System.out.println("list = " + list));

        fileList.forEach(vo -> {
            Path path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName());
            checkFilePath.add(path);
        });

        System.out.println("CheckFilePath 썸네일");
        checkFilePath.forEach(list -> System.out.println("list = " + list));

        File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
        File[] targetFiles = targetDir.listFiles();

        System.out.println("targetFile : ");
        for (File file : targetFiles) {
            System.out.println("file = " + file);
        }

        List<File> removeFileList = new ArrayList<File>(Arrays.asList(targetFiles));

        for (File file : targetFiles) {
            checkFilePath.forEach(checkFile ->{
                if (file.toPath().equals(checkFile)) {
                    removeFileList.remove(file);
                }
            });
        }

        System.out.println("remover file filter after........");
        removeFileList.forEach(file -> {
            System.out.println("file = " + file);
        });

        /* 파일 삭제 */
        for (File file : removeFileList) {
            System.out.println("remove file: " + file);
            file.delete();
        }
    }

    private String getFolderYesterDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, -1);
        String str = sdf.format(cal.getTime());
        return str.replace("-", File.separator);


    }

}

