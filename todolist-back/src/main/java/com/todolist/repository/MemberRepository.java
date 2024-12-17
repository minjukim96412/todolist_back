package com.todolist.repository;

import com.todolist.model.MemberEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    // 이메일 중복 체크
	MemberEntity findByEmail(String email);

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);
}


