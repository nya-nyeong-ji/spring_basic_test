package com.example.spring_basic_test.domain.repository;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.entity.QMemberEntity;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public class MemberCustomRepositoryImpl extends QuerydslRepositorySupport implements MemberCustomRepository {

    public MemberCustomRepositoryImpl() {
        super(MemberEntity.class);
    }

    @Override
    public List<MemberEntity> findRecentlyRegistered(int limit) {
        final QMemberEntity member = QMemberEntity.memberEntity;
        return from(member)
                .limit(limit)
                .orderBy(member.createDate.desc())
                .fetch();
    }
}
