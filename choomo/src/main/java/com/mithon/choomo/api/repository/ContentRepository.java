package com.mithon.choomo.api.repository;

import com.mithon.choomo.api.entity.ContentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<ContentEntity, Long> {

    // 복합 검색 (제목, 내용, 위치, 회원)
    @Query("SELECT DISTINCT c FROM ContentEntity c " +
            "LEFT JOIN FETCH c.member " +
            "WHERE (:keyword IS NULL OR c.title LIKE %:keyword% OR c.content LIKE %:keyword%) " +
            "AND (:memberId IS NULL OR c.member.id = :memberId) " +
            "AND (:location IS NULL OR c.location LIKE %:location%)")
    Page<ContentEntity> searchContents(
            @Param("keyword") String keyword,
            @Param("memberId") Long memberId,
            @Param("location") String location,
            Pageable pageable
    );

    // 해시태그로 게시물 검색
    @Query("SELECT DISTINCT c FROM ContentEntity c " +
            "JOIN c.hashtags h " +
            "WHERE h.content = :hashtagContent")
    Page<ContentEntity> findByHashtagContent(
            @Param("hashtagContent") String hashtagContent,
            Pageable pageable
    );

    // 특정 회원의 게시물 조회
    Page<ContentEntity> findByMemberId(Long memberId, Pageable pageable);
}