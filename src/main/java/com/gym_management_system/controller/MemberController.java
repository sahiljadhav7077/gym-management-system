package com.gym_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gym_management_system.entity.Member;
import com.gym_management_system.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/register")
    public String registerPage(Model model) {
    model.addAttribute("member", new Member());
    return "register";
    }


    @PostMapping("/register")
     public String register(@Valid Member member,
                       BindingResult result,
                       Model model) {

    if (result.hasErrors()) {
        return "register";
    }

    if(memberService.emailExists(member.getEmail())){

    model.addAttribute("emailError",
    "Email already registered");

    return "register";
}

    memberService.saveMember(member);

    return "redirect:/login";
}


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

        @PostMapping("/login")
        public String login(
        @RequestParam String email,
        @RequestParam String password,
        HttpSession session,
        Model model) {

            Member member = memberService.login(email, password);

            if (member != null) {

            session.setAttribute("loggedInMember", member);

            return "redirect:/dashboard";
    }

    model.addAttribute("error", "Invalid Email or Password");

    return "login";
    }

    @GetMapping("/dashboard")
public String dashboard(HttpSession session, Model model) {

    if (session.getAttribute("loggedInMember") == null) {
        return "redirect:/login";
    }

    model.addAttribute("totalMembers", memberService.getTotalMembers());
    model.addAttribute("basicMembers", memberService.getBasicMembers());
    model.addAttribute("premiumMembers", memberService.getPremiumMembers());
    model.addAttribute("eliteMembers", memberService.getEliteMembers());

    return "dashboard";
}

    // View All Members
    @GetMapping("/members")
    public String viewMembers(Model model) {

        List<Member> members = memberService.getAllMembers();

        model.addAttribute("members", members);

        return "members";
    }

    // Edit Member
    @GetMapping("/edit/{id}")
    public String editMember(@PathVariable Long id, Model model) {

        Member member = memberService.getMemberById(id);

        model.addAttribute("member", member);

        return "edit-member";
    }

    // Update Member
    @PostMapping("/update")
    public String updateMember(Member member) {

        memberService.updateMember(member);

        return "redirect:/members";
    }

    // Delete Member
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {

        memberService.deleteMember(id);

        return "redirect:/members";
    }

    @GetMapping("/search")
    public String searchMembers(@RequestParam String keyword,
                            Model model){

    model.addAttribute("members",
            memberService.searchMembers(keyword));

    return "members";

    }

    @GetMapping("/logout")
public String logout(HttpSession session) {

    session.invalidate();

    return "redirect:/login";
}
}