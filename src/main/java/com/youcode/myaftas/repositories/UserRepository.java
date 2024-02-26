package com.youcode.myaftas.repositories;

import com.youcode.myaftas.entities.Member;
import com.youcode.myaftas.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    List<User> findByName(String name);
    List<User> findByFamilyName(String familyName);
}
