package com.vam.controller;

import com.vam.domain.MemberVO;
import com.vam.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder pwEncoder;

    @RequestMapping(value = "join", method = RequestMethod.GET)
    public void loginGET() {
        logger.info("회원가입 페이지 진입");
    }

    //회원가입
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String joinPOST(MemberVO member) throws Exception {

        logger.info("join 진입");

        String rawPw = "";
        String encodePw = "";

        rawPw = member.getMemberPw();
        encodePw = pwEncoder.encode(rawPw);
        member.setMemberPw(encodePw);

        //회원가입 서비스 실행
        memberService.memberJoin(member);

        logger.info("join Service 성공");

        return "redirect:/main";
    }

    @RequestMapping(value = "/memberIdChk", method = RequestMethod.POST)
    @ResponseBody
    public String memberIdChkPOST(String memberId) throws Exception {
//        logger.info("memberIdChk() 진입");

        int result = memberService.idCheck(memberId);

//        logger.info("결과 값 = " + result);

        if (result != 0) {
            return "fail";
        } else {
            return "success";
        }

    }

    @RequestMapping(value = "/mailCheck", method = RequestMethod.GET)
    @ResponseBody
    public String mailCheckGET(String email) throws Exception {

        /* View 로부터 넘어온 데이터 확인*/
        logger.info("이메일 데이터 전송 확인");
        logger.info("인증번호 : " + email);

        Random random = new Random();
        int checkNum = random.nextInt(888888) + 111111;
        logger.info("인증번호" + checkNum);

        /* 이메일 보내기 */
        String setFrom = "lung4536@gmail.com";
        String toMail = email;
        String title = "회원가입 인증 이메일 입니다.";
        String content =
                "홈페이지를 방문해주셔서 감사합니다." +
                        "<br></br>" +
                        "인증 번호는 " + checkNum + " 입니다." +
                        "<br>" +
                        "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(setFrom);
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String num = Integer.toString(checkNum);

        return num;
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void joinGET() {

        logger.info("로그인 페이지 진입");

    }

    /* 로그인 */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception {

        HttpSession session = request.getSession();
        String rawPw = "";
        String encodePw = "";

        MemberVO lvo = memberService.memberLogin(member);

        if (lvo != null) {  // 일치하는 아이디 존재시
            rawPw = member.getMemberPw();   // 사용자가 제출한 비밀번호
            encodePw = lvo.getMemberPw();   // 데이터베이스에 저장한 인코딩 비밀번호

            if (true == pwEncoder.matches(rawPw, encodePw)) {   //비밀번호 일치 여부 판단
                lvo.setMemberPw("");    // 인코딩된 비밀번호 정보 지워줌
                session.setAttribute("member", lvo);    // session에 사용자 정보 저장
                return "redirect:/main";
            } else {
                rttr.addFlashAttribute("result", 0);
                return "redirect:/member/login";
            }
        } else {        // 일치하는 아이디가 존재하지 않을 시
            rttr.addFlashAttribute("result", 0);
            return "redirect:/member/login";
        }
    }

    /* 메인페이지 로그아웃 */
    @RequestMapping(value = "/logout.do", method = RequestMethod.GET)
    public String logoutMainGET(HttpServletRequest request) throws Exception {
        logger.info("logoutMainGET 메서드 진입");

        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/main";
    }

    /* 비동기방식 로그아웃 메서드 */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public void logoutPOST(HttpServletRequest request) throws Exception {

        logger.info("비동기 로그아웃 메서드 진입");

        HttpSession session = request.getSession();
        session.invalidate();

    }
}
