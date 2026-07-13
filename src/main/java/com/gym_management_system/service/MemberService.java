package com.gym_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gym_management_system.entity.Member;
import com.gym_management_system.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // Save Member (Register)
    public Member saveMember(Member member){

    member.setPassword(passwordEncoder.encode(member.getPassword()));

    return memberRepository.save(member);

}


    // Login
    public Member login(String email, String password) {

        Optional<Member> member = memberRepository.findByEmail(email);

        if (member.isPresent()) {

            if(passwordEncoder.matches(password, member.get().getPassword())) {
                return member.get();
            }

        }

        return null;
    }

    // View All Members
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Get Member By ID
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    // Update Member
    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    // Delete Member
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public boolean emailExists(String email){

    return memberRepository.existsByEmail(email);
    }

    public long getTotalMembers() {
    return memberRepository.count();
    }

    public long getBasicMembers() {
    return memberRepository.countByMembership("Basic");
    }

    public long getPremiumMembers() {
    return memberRepository.countByMembership("Premium");
    }

    public long getEliteMembers() {
    return memberRepository.countByMembership("Elite");
    }

    public List<Member> searchMembers(String name){

    return memberRepository.findByNameContainingIgnoreCase(name);

    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

}