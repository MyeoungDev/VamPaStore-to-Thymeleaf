package com.vam.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Data
public class AuthorVO {

    /* 작가 아이디 */
    private int authorId;

    /* 작가 이름 */
    private String authorName;

    /* 국가 id */
    private String nationId;

    /* 작가 국가 */
    private String nationName;

    /* 작가 소개 */
    private String authorIntro;

    /* 등록 날짜 */
    private Date regDate;

    /* 수정 날짜 */
    private Date updateDate;

    public void setNationId(String nationId) {
        this.nationId = nationId;
        if (nationId.equals("01")) {
            this.nationName = "국내";
        } else {
            this.nationName = "국외";
        }
    }
}
