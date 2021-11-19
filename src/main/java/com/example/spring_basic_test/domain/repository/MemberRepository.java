package com.example.spring_basic_test.domain.repository;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String>, MemberCustomRepository, QuerydslPredicateExecutor<MemberEntity> {

    MemberEntity findByEmail(Email email);
}
