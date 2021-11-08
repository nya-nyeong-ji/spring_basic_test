package com.example.spring_basic_test.service;

import com.example.spring_basic_test.domain.entity.MemberEntity;
import com.example.spring_basic_test.domain.entity.QMemberEntity;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberSearchService extends QuerydslRepositorySupport {

    public MemberSearchService() {
        super(MemberEntity.class);
    }

    public Page<MemberEntity> search(final MemberSearchType type, final String value, final Pageable pageable) {
        final QMemberEntity member = QMemberEntity.memberEntity;
        final JPQLQuery<MemberEntity> query;

        switch (type) {
            case EMAIL:
                query = from(member)
                        .where(member.email.address.likeIgnoreCase(value + "%"));
                break;
            case NICKNAME:
                query = from(member)
                        .where(member.nickname.likeIgnoreCase(value + "%"));
                break;
            case ALL:
                query = from(member)
                        .fetchAll();
                break;
            default:
                throw new IllegalArgumentException();
        }

        final List<MemberEntity> list = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(list, pageable, query.fetchCount());
    }
}
