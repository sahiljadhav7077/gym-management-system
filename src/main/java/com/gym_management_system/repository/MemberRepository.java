package com.gym_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gym_management_system.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByMembership(String membership);

    List<Member> findByNameContainingIgnoreCase(String name);
}