package kh.petmily.controller;

import kh.petmily.domain.find_board.FindBoard;
import kh.petmily.domain.find_board.form.FindBoardPageForm;
import kh.petmily.domain.look_board.form.LookBoardPageForm;
import kh.petmily.domain.member.form.JoinRequest;
import kh.petmily.domain.member.form.MemberChangeForm;
import kh.petmily.service.FindBoardService;
import kh.petmily.service.LookBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import kh.petmily.domain.member.Member;
import kh.petmily.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final FindBoardService findBoardService;
    private final LookBoardService lookBoardService;

    // 회원 가입
    @GetMapping("/join")
    public String joinForm() {
        return "/login/joinForm";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("joinRequest") JoinRequest joinRequest) {
        log.info("넘어온 joinRequest : {}", joinRequest);

        if (!joinRequest.isPwEqualToConfirm()) {
            return "login/joinForm";
        }

        memberService.join(joinRequest);

        return "/login/loginForm";
    }

    // 로그인
    @GetMapping("/login")
    public String loginForm() {
        return "/login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("id") String id,
            @RequestParam("pw") String pw,
            HttpServletRequest request) {

        Member authUser = memberService.login(id, pw);

        if (authUser == null) {
            return "/login/loginForm";
        }

        request.getSession().setAttribute("authUser", authUser);

        return "/main/index";
    }

    // 로그아웃
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();

        return "/main/index";
    }

    @RequestMapping("/")
    public String home() {
        return "/main/index";
    }

    // mypage
    @GetMapping("/member/mypage")
    public String mypage(HttpServletRequest request, Model model) {
        Member member = getAuthMember(request);

        model.addAttribute("member", member);

        return "/member/mypage";
    }

    //change member Info
    @GetMapping(value = "/member/change_info")
    private String changeInfo(HttpServletRequest request, Model model) {
        Member member = getAuthMember(request);

        model.addAttribute("memberInfo", member);

        return "/member/changeMemberInfo";
    }

    @PostMapping(value = "/member/change_info")
    private String changeInfoPost(HttpServletRequest request, Model model, MemberChangeForm memberChangeForm) {
        Member member = getAuthMember(request);

        Member mem = memberService.modify(member, memberChangeForm);

        model.addAttribute("member", mem);
        model.addAttribute("authUser", mem);

        return "/member/mypage";
    }

    private static Member getAuthMember(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute("authUser");

        return member;
    }

    // 회원탈퇴
    @GetMapping("/member/withdraw")
    public String withdrawForm() {
        return "/member/withdrawForm";
    }

    @PostMapping("/member/withdraw")
    public String withdraw(HttpServletRequest request, @RequestParam String pw, @RequestParam String confirmPw) {

        log.info("pw = {}", pw);
        log.info("confirmPw = {}", confirmPw);

        Member member = getAuthMember(request);
        int mNumber = member.getMNumber();

        Map<String, Boolean> errors = new HashMap<>();
        request.setAttribute("errors", errors);

        if (!memberService.isPwEqualToConfirm(pw, confirmPw)) {
            errors.put("notMatch", Boolean.TRUE);
            return "/member/withdrawForm";
        } else if (!memberService.checkPwCorrect(mNumber, pw)) {
            errors.put("notCorrect", Boolean.TRUE);
            return "/member/withdrawForm";
        }

        memberService.withdraw(mNumber);
        request.getSession().invalidate();

        return "/member/withdrawSuccess";
    }

    //찾아요 게시판 매칭 결과
    @GetMapping("/member/checkMatching")
    public String checkMatching(@RequestParam(required = false) String matched, HttpServletRequest request, Model model) {
        String pageNoVal = request.getParameter("pageNo");

        int pageNo = 1;

        if (pageNoVal != null) {
            pageNo = Integer.parseInt(pageNoVal);
        }

        Member member = getAuthMember(request);
        int mNumber = member.getMNumber();

        FindBoardPageForm Finds = findBoardService.getMembersFindPage(pageNo, mNumber, matched);
        model.addAttribute("Finds", Finds);

        request.getSession().setAttribute("matched", matched);

        return "/member/listFindBoard";
    }

    @GetMapping("/member/checkMatching/lookList")
    public String checkMatchingDetail(@RequestParam("faNumber") int faNumber, HttpServletRequest request, Model model) {
        FindBoard findBoard = findBoardService.getFindBoard(faNumber);
        String pageNoVal = request.getParameter("pageNo");

        int pageNo = 1;

        if (pageNoVal != null) {
            pageNo = Integer.parseInt(pageNoVal);
        }

        LookBoardPageForm boardPage = lookBoardService.getMatchedLookPage(pageNo, findBoard);
        model.addAttribute("matchedLookBoardForm", boardPage);

        return "/member/listLookBoard";
    }
}