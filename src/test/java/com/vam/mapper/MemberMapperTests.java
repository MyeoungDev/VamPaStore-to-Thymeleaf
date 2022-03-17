package com.vam.mapper;

import com.vam.domain.MemberVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberMapperTests {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    public void memberJoin() throws Exception {
        MemberVO member = new MemberVO();

        member.setMemberId("test");
        member.setMemberPw("test");
        member.setMemberName("test");
        member.setMemberMail("test");
        member.setMemberAddr1("test");
        member.setMemberAddr2("test");
        member.setMemberAddr3("test");

        memberMapper.memberJoin(member);

    }

    @Test
    public void memberIdChk() {
        String id = "admin";
        String id2 = "test123";
        memberMapper.idCheck(id);
        memberMapper.idCheck(id2);
    }

    /* 로그인 쿼리 mapper 메서드 테스트 */
    @Test
    public void memberLogin() {
        MemberVO member = new MemberVO();
        member.setMemberId("test1");
        member.setMemberPw("test1");

        memberMapper.memberLogin(member);
        System.out.println("결과 값: " + memberMapper.memberLogin(member));
    }

}
