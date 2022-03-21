package com.vam.service;

import com.vam.domain.AuthorVO;
import com.vam.domain.Criteria;
import com.vam.mapper.AuthorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorMapper authorMapper;

    private final static Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public void authorEnroll(AuthorVO author) throws Exception {
        authorMapper.authorEnroll(author);
    }

    @Override
    public List<AuthorVO> authorGetList(Criteria cri) throws Exception {
        logger.info("(service)authorGetList()........... " + cri);
        return authorMapper.authorGetList(cri);
    }

    @Override
    public int authorGetTotal(Criteria cri) throws Exception {
        logger.info("service authorGetTotal");
        return authorMapper.authorGetTotal(cri);
    }

    @Override
    public AuthorVO authorGetDetail(int authorId) throws Exception {
        logger.info("service authorGetDetail");
        return authorMapper.authorGetDetail(authorId);
    }

    @Override
    public int authorModify(AuthorVO author) throws Exception {
        logger.info("service authorModify");
        return authorMapper.authorModify(author);
    }

    @Override
    public int authorDelete(int authorId) {
        logger.info("Service authorDelete");

        return authorMapper.authorDelete(authorId);
    }
}
