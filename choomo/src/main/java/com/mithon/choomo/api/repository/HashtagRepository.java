package com.mithon.choomo.api.repository;

import com.mithon.choomo.api.entity.HashtagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {

    // 게시물의 모든 해시태그 조회
    List<HashtagEntity> findByContentEntityId(Long contentId);

    // 게시물과 내용으로 해시태그 조회 (중복 체크용)
    boolean existsByContentEntityIdAndContent(Long contentId, String content);

    // 복합 검색
    @Query("SELECT h FROM HashtagEntity h " +
            "LEFT JOIN FETCH h.member " +
            "LEFT JOIN FETCH h.contentEntity " +
            "WHERE (:content IS NULL OR h.content LIKE %:content%) " +
            "AND (:contentId IS NULL OR h.contentEntity.id = :contentId) " +
            "AND (:memberId IS NULL OR h.member.id = :memberId)")
    Page<HashtagEntity> searchHashtags(
            @Param("content") String content,
            @Param("contentId") Long contentId,
            @Param("memberId") Long memberId,
            Pageable pageable
    );

    // 인기 해시태그 조회 (사용 빈도순)
    @Query("SELECT h.content, COUNT(h) as cnt FROM HashtagEntity h " +
            "GROUP BY h.content " +
            "ORDER BY cnt DESC")
    Page<Object[]> findPopularHashtags(Pageable pageable);

    // 게시물의 해시태그 일괄 삭제
    void deleteByContentEntityId(Long contentId);
}