package com.example.spring_basic_test.domain.repository;

import com.example.spring_basic_test.domain.entity.MemberEntity;

import java.util.List;

public interface MemberCustomRepository {
    List<MemberEntity> findRecentlyRegistered(int limit);
}
