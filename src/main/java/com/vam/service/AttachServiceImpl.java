package com.vam.service;

import com.vam.domain.AttachImageVo;
import com.vam.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttachServiceImpl implements AttachService{

    @Autowired
    private AttachMapper attachMapper;

    @Override
    public List<AttachImageVo> getAttachList(int bookId) {
        return attachMapper.getAttachList(bookId);
    }
}
