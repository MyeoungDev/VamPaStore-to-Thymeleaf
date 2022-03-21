package com.vam.controller;

import com.google.gson.Gson;
import com.vam.domain.*;
import com.vam.service.AdminService;
import com.vam.service.AuthorService;
import org.dom4j.rule.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AdminService adminService;

    /* 관리자 메인 페이지 이동 */
    @RequestMapping(value = "main", method = RequestMethod.GET)
    public void adminMainGET() throws Exception {

        logger.info("관리자 페이지 이동");

    }

    /* 상품 등록 페이지 접속 */
    @RequestMapping(value = "goodsEnroll", method = RequestMethod.GET)
    public void goodsEnrollGET(Model model) throws Exception {
        logger.info("상품 등록 페이지 접속");

        List<CateVO> list = adminService.cateList();

        Gson gson = new Gson();

        String cateList = gson.toJson(list);

        model.addAttribute("cateList", cateList);
    }

    /* 상품 관리 페이지 접속 */
    @RequestMapping(value = "goodsManage", method = RequestMethod.GET)
    public void goodsManageGET(Criteria cri, Model model) throws Exception {
        logger.info("상품 관리 페이지 접속");

        List<BookVO> list = adminService.goodsGetList(cri);

        if (!list.isEmpty()) {
            model.addAttribute("list", list);
        } else {
            model.addAttribute("listCheck", "empty");
        }

        int total = adminService.goodsGetTotal(cri);
        PageDTO pageMaker = new PageDTO(cri, total);

        model.addAttribute("pageMaker", pageMaker);
    }

    /* 상품 조회(Detail) 페이지 */
    @RequestMapping(value = "goodsDetail", method = RequestMethod.GET)
    public void goodsDetailGET(
            @RequestParam(value = "bookId", required = false) int bookId,
            Criteria cri,
            Model model
    ) {
        logger.info("goodsDetailGET().........." + bookId);

        /* 카테고리 리스트 데이터 */
        Gson gson = new Gson();
        String cateList = gson.toJson(adminService.cateList());
        model.addAttribute("cateList", cateList);

        /* 목록 페이지 조건 정보 */
        model.addAttribute("cri", cri);

        /* 조회 페이지 정보 */
        BookVO bookInfo = adminService.goodsGetDetail(bookId);

        model.addAttribute("goodsInfo", bookInfo);
    }

    @RequestMapping(value = "goodsModify", method = RequestMethod.GET)
    public void goodsModifyGET(
            @RequestParam(value = "bookId", required = false) int bookId,
            Criteria cri,
            Model model
    ) {
        logger.info("goodsModifyGET.........." + bookId);
        Gson gson = new Gson();
        String cateList = gson.toJson(adminService.cateList());

        model.addAttribute("cateList", cateList);

        model.addAttribute("goodsInfo", adminService.goodsGetDetail(bookId));

    }

    @RequestMapping(value = "/goodsModify", method = RequestMethod.POST)
    public String goodsModifyPOST(
            BookVO book,
            RedirectAttributes rttr
    ) {

        logger.info("goodsModifyPOST............" + book);

        int result = adminService.goodsModify(book);

        rttr.addFlashAttribute("goodsModify_result", result);

        return "redirect:/admin/goodsManage";
    }

    @RequestMapping(value = "/goodsDelete", method = RequestMethod.POST)
    public String goodsDeletePOST(int bookId, RedirectAttributes rttr) {

        logger.info("goodsDeletePOST.........." + bookId);

        int result = adminService.goodsDelete(bookId);

        rttr.addFlashAttribute("goodsDelete_result", result);

        return "redirect:/admin/goodsManage";
    }


    /* 작가 등록 페이지 접속 */
    @RequestMapping(value = "authorEnroll", method = RequestMethod.GET)
    public void authorEnrollGET() throws Exception {

        logger.info("작가 등록 페이지 접속");
    }

    /* 작가 등록 */
    @RequestMapping(value = "authorEnroll.do", method = RequestMethod.POST)
    public String authorEnrollPOST(AuthorVO author, RedirectAttributes rttr) throws Exception {

        logger.info("authorEnroll : " + author);

        authorService.authorEnroll(author);

        /* redirect 전송시 데이터 매핑해서 보내줌 */
        /* 등록 성공 메시지 (작가이름) */
        rttr.addFlashAttribute("enroll_result", author.getAuthorName());

        return "redirect:/admin/authorManage";
    }

    /* 작가 관리 페이지 접속 */
    @RequestMapping(value = "authorManage", method = RequestMethod.GET)
    public void authorManageGET(Criteria cri, Model model) throws Exception {

        logger.info("작가 관리 페이지 접속 ..........." + cri);

        List<AuthorVO> list = authorService.authorGetList(cri);

        if (!list.isEmpty()) {
            model.addAttribute("list", list);
        } else {
            model.addAttribute("listCheck", "empty");
        }

        /* 페이지 이동 인터페이스 데이터 */
        int total = authorService.authorGetTotal(cri);

        PageDTO pageMaker = new PageDTO(cri, total);

        model.addAttribute("pageMaker", pageMaker);

    }

    /* 작가 상세 페이지 */
    @RequestMapping(value = "/authorDetail", method = RequestMethod.GET)
    public void authorGetInfoGET(
            @RequestParam(value = "authorId", required = false) int authorId,
            Criteria cri,
            Model model
    ) throws Exception {

        logger.info("authorDetail..........." + authorId);

        /* 작가 관리 페이지 정보 */
        model.addAttribute("cri", cri);     // 상세 페이지에서 관리 페이지로 이동하기위함

        /* 선택 작가 정보 */
        AuthorVO authorInfo = authorService.authorGetDetail(authorId);

        model.addAttribute("authorInfo", authorInfo);
    }

    /* 작가 수정 페이지 */
    @RequestMapping(value = "/authorModify", method = RequestMethod.GET)
    public void authorModifyGET(
            @RequestParam(value = "authorId", required = false) int authorId,
            Criteria cri,
            Model model
    ) throws Exception {
        logger.info("authorModify......." + authorId);
        model.addAttribute("cri", cri);

        AuthorVO authorInfo = authorService.authorGetDetail(authorId);
        model.addAttribute("authorInfo", authorInfo);
    }

    @RequestMapping(value = "/authorModify", method = RequestMethod.POST)
    public String authorModifyPOST(
            @RequestParam(value = "authorId", required = false) int authorId,
            AuthorVO author,
            RedirectAttributes rttr
    ) throws Exception {

        logger.info("authorModifyPOST" + author);

        int result = authorService.authorModify(author);

        rttr.addFlashAttribute("modify_result", result);

        return "redirect:/admin/authorManage";
    }


    /* 상품 등록 */
    @RequestMapping(value = "/goodsEnroll", method = RequestMethod.POST)
    public String goodsEnrollPOST(BookVO book, RedirectAttributes rttr) {

        logger.info("goodsEnrollPOST....." + book);

        adminService.bookEnroll(book);

        rttr.addFlashAttribute("bookEnroll_result", book.getBookName());

        return "redirect:/admin/goodsManage";
    }

    /* 작가 찾기 */
    @RequestMapping(value = "/authorPop", method = RequestMethod.GET)
    public void authorPopGET(Criteria cri, Model model) throws Exception {
        logger.info("authorPopGET.......");

        cri.setAmount(5);

        List<AuthorVO> list = authorService.authorGetList(cri);

        if (!list.isEmpty()) {
            model.addAttribute("list", list);
        } else {
            model.addAttribute("listCheck", "empty");
        }

        int total = authorService.authorGetTotal(cri);

        PageDTO pageMaker = new PageDTO(cri, total);

        model.addAttribute("pageMaker", pageMaker);

    }
}
