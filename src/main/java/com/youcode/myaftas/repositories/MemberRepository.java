package com.youcode.myaftas.repositories;

import com.youcode.myaftas.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findByName(String name);
    List<Member> findByFamilyName(String familyName);
}
