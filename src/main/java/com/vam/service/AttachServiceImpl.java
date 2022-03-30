package com.vam.service;

import com.vam.domain.AttachImageVo;
import com.vam.mapper.AttachMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AttachServiceImpl implements AttachService{

    @Autowired
    private AttachMapper attachMapper;

    @Override
    public List<AttachImageVo> getAttachList(int bookId) {
        log.info("Service getAttachList..........");
        return attachMapper.getAttachList(bookId);
    }
}
