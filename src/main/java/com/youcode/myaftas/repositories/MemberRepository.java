package com.youcode.myaftas.repositories;

import com.youcode.myaftas.entities.Member;
import com.youcode.myaftas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<User> findByUsername(String username);
    List<Member> findByName(String name);
    List<Member> findByFamilyName(String familyName);
}
