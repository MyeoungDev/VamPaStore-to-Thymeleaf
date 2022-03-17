package com.vam.domain;

import lombok.Data;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class Criteria {

    /* 현재 페이지 번호 */
    private int pageNum;

    /* 페이지 표시 개수 */
    private int amount;

    /* 페이지 skip */
    private int skip;

    /* 검색 타입 */
    private String type;

    /* 검색 키워드 */
    private String keyword;

    /* Criteria 생성자 */
    public Criteria(int pageNum, int amount) {
        this.pageNum = pageNum;
        this.amount = amount;
        this.skip = (pageNum - 1) * amount;
    }

    /* Criteria 기본 생성자 */
    public Criteria() {
        this(1, 10);
    }

    /* 검색 타입 데이터 배열 변환 */
    public String[] getTypeArr() {
        return type == null ? new String[]{} : type.split("");
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        this.skip = (pageNum - 1) * this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        this.skip = (this.pageNum - 1) * amount;
    }

    public String makeQueryString(int pageNum) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                /* pageNum 이랑 searchType and keyword 이거 3개만 설정 하면 나머지는 건들지 않아도 된다? */
                // 현재 페이지 번호
                .queryParam("pageNum", pageNum)
                .queryParam("amount", amount)
                .queryParam("searchType", type)
                .queryParam("keyword", keyword)
                .build()
                .encode();

        return uriComponents.toUriString();
    }

}
