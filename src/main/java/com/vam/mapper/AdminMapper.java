package com.vam.mapper;

import com.vam.domain.AttachImageVo;
import com.vam.domain.BookVO;
import com.vam.domain.CateVO;
import com.vam.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    /* 상품 등록 */
    public void bookEnroll(BookVO book);

    /* 카테고리 리스트 */
    public List<CateVO> cateList();

    /* 상품 리스트 */
    public List<BookVO> goodsGetList(Criteria cri);

    /* 상품 총 개수 */
    public int goodsGetTotal(Criteria cri);

    /* 상품 조회 페이지 */
    public BookVO goodsGetDetail(int bookId);

    /* 상품 수정 */
    public int goodsModify(BookVO book);

    /* 상품 삭제 */
    public int goodsDelete(int bookId);

    /* 이미지 등록 */
    public void imageEnroll(AttachImageVo vo);

    /* 지정 상품 이미지 전체 삭제 */
    public void deleteImgAll(int bookId);

    /* 어제자 날짜 이미지 리스트 */
    public List<AttachImageVo> checkFileList();
}
