package com.mithon.choomo.api.service;

import com.mithon.choomo.api.dto.req.HashtagCreateReq;
import com.mithon.choomo.api.dto.req.HashtagEditReq;
import com.mithon.choomo.api.dto.req.HashtagSearchReq;
import com.mithon.choomo.api.dto.res.HashtagRes;
import com.mithon.choomo.api.dto.res.PageRes;
import com.mithon.choomo.api.entity.ContentEntity;
import com.mithon.choomo.api.entity.HashtagEntity;
import com.mithon.choomo.api.entity.Member;
import com.mithon.choomo.api.repository.ContentRepository;
import com.mithon.choomo.api.repository.HashtagRepository;
import com.mithon.choomo.api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final ContentRepository contentRepository;
    private final MemberRepository memberRepository;

    /**
     * 해시태그 생성
     */
    public HashtagRes createHashtag(HashtagCreateReq request) {
        log.info("해시태그 생성 요청: {}", request);

        // 게시물 존재 확인
        ContentEntity content = contentRepository.findById(request.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. ID: " + request.getContentId()));

        // 회원 존재 확인
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + request.getMemberId()));

        // 중복 체크
        if (hashtagRepository.existsByContentEntityIdAndContent(request.getContentId(), request.getContent())) {
            throw new IllegalArgumentException("이미 존재하는 해시태그입니다: " + request.getContent());
        }

        // 해시태그 생성
        HashtagEntity hashtag = HashtagEntity.builder()
                .content(request.getContent().trim())
                .member(member)
                .contentEntity(content)
                .build();

        HashtagEntity savedHashtag = hashtagRepository.save(hashtag);

        log.info("해시태그 생성 완료: ID={}", savedHashtag.getId());
        return convertToResponse(savedHashtag);
    }

    /**
     * 해시태그 조회
     */
    @Transactional(readOnly = true)
    public HashtagRes getHashtag(Long id) {
        log.info("해시태그 조회: ID={}", id);

        HashtagEntity hashtag = hashtagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그입니다. ID: " + id));

        return convertToResponse(hashtag);
    }

    /**
     * 해시태그 검색
     */
    @Transactional(readOnly = true)
    public PageRes<HashtagRes> searchHashtags(HashtagSearchReq request) {
        log.info("해시태그 검색: {}", request);

        Sort sort = Sort.by(Sort.Direction.DESC, "regTime");
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<HashtagEntity> hashtagPage = hashtagRepository.searchHashtags(
                request.getContent(),
                request.getContentId(),
                request.getMemberId(),
                pageable
        );

        List<HashtagRes> hashtags = hashtagPage.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageRes.<HashtagRes>builder()
                .content(hashtags)
                .page(hashtagPage.getNumber())
                .size(hashtagPage.getSize())
                .totalElements(hashtagPage.getTotalElements())
                .totalPages(hashtagPage.getTotalPages())
                .last(hashtagPage.isLast())
                .build();
    }

    /**
     * 해시태그 수정
     */
    public HashtagRes editHashtag(Long id, HashtagEditReq request) {
        log.info("해시태그 수정: ID={}, Request={}", id, request);

        HashtagEntity hashtag = hashtagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 해시태그입니다. ID: " + id));

        // 중복 체크 (같은 게시물 내에서)
        if (hashtagRepository.existsByContentEntityIdAndContent(
                hashtag.getContentEntity().getId(),
                request.getContent())) {
            throw new IllegalArgumentException("이미 존재하는 해시태그입니다: " + request.getContent());
        }

        hashtag.updateContent(request.getContent().trim());
        HashtagEntity updatedHashtag = hashtagRepository.save(hashtag);

        log.info("해시태그 수정 완료: ID={}", id);
        return convertToResponse(updatedHashtag);
    }

    /**
     * 해시태그 삭제
     */
    public void deleteHashtag(Long id) {
        log.info("해시태그 삭제: ID={}", id);

        if (!hashtagRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 해시태그입니다. ID: " + id);
        }

        hashtagRepository.deleteById(id);
        log.info("해시태그 삭제 완료: ID={}", id);
    }

    // ========== 변환 메서드 ==========

    private HashtagRes convertToResponse(HashtagEntity hashtag) {
        return HashtagRes.builder()
                .id(hashtag.getId())
                .content(hashtag.getContent())
                .contentId(hashtag.getContentEntity().getId())
                .contentTitle(hashtag.getContentEntity().getTitle())
                .memberId(hashtag.getMember().getId())
                .memberName(hashtag.getMember().getName())
                .regTime(hashtag.getRegTime())
                .build();
    }
}