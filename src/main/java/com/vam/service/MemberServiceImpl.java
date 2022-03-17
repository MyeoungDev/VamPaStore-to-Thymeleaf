package com.vam.service;

import com.vam.domain.MemberVO;
import com.vam.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public void memberJoin(MemberVO member) throws Exception {
        memberMapper.memberJoin(member);
    }

    @Override
    public int idCheck(String memberId) throws Exception {
        return memberMapper.idCheck(memberId);
    }

    @Override
    public MemberVO memberLogin(MemberVO member) throws Exception {
        return memberMapper.memberLogin(member);
    }
}
