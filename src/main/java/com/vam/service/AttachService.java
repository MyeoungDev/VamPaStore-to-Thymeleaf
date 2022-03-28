package com.vam.service;

import com.vam.domain.AttachImageVo;

import java.util.List;

public interface AttachService {

    /* 이미지 데이터 반환 */
    public List<AttachImageVo> getAttachList(int bookId);
}
