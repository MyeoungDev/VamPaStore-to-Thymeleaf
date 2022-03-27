package com.vam.service;

import com.vam.domain.AttachImageVo;
import com.vam.domain.BookVO;
import com.vam.domain.CateVO;
import com.vam.domain.Criteria;
import com.vam.mapper.AdminMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private final static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void bookEnroll(BookVO book) {

        logger.info("(service) bookEnroll...........");

        if (book.getImageList() == null || book.getImageList().size() < 0) {
            return;
        }

//        book.getImageList().forEach(attach ->{
//            attach.setBookId(book.getBookId());
//            adminMapper.imageEnroll(attach);
//        });


        adminMapper.bookEnroll(book);

        for (AttachImageVo attachImageVo : book.getImageList()) {
            attachImageVo.setBookId(book.getBookId());

            adminMapper.imageEnroll(attachImageVo);
        }
    }

    @Override
    public List<CateVO> cateList() {

        logger.info("(service) cateList..............");

        return adminMapper.cateList();
    }

    @Override
    public List<BookVO> goodsGetList(Criteria cri) {
        logger.info("Service goodsGetList............");
        return adminMapper.goodsGetList(cri);
    }

    @Override
    public int goodsGetTotal(Criteria cri) {
        logger.info("Service goodsGetTotal");
        return adminMapper.goodsGetTotal(cri);
    }

    @Override
    public BookVO goodsGetDetail(int bookId) {
        logger.info("Service goodsGetDetail");

        return adminMapper.goodsGetDetail(bookId);
    }

    @Override
    public int goodsModify(BookVO book) {
        logger.info("Service goodsModify");

        return adminMapper.goodsModify(book);
    }

    @Override
    public int goodsDelete(int bookId) {
        logger.info("Service goodsDelete");

        return adminMapper.goodsDelete(bookId);
    }
}
