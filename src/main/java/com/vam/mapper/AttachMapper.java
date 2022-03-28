package com.vam.mapper;

import com.vam.domain.AttachImageVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttachMapper {

    /* 이미지 데이터 반환 */
    public List<AttachImageVo> getAttachList(int bookId);
}
