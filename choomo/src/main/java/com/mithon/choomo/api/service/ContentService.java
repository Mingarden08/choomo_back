package com.mithon.choomo.api.service;

import com.mithon.choomo.api.dto.req.ContentCreateReq;
import com.mithon.choomo.api.dto.req.ContentEditReq;
import com.mithon.choomo.api.dto.req.ContentSearchReq;
import com.mithon.choomo.api.dto.res.ContentListRes;
import com.mithon.choomo.api.dto.res.ContentRes;
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
public class ContentService {

    private final ContentRepository contentRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시물 생성
     */
    public ContentRes createContent(ContentCreateReq request) {
        log.info("게시물 생성 요청: {}", request);

        // 회원 존재 확인
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + request.getMemberId()));

        // 게시물 생성
        ContentEntity content = ContentEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .location(request.getLocation())
                .member(member)
                .build();

        ContentEntity savedContent = contentRepository.save(content);

        // 해시태그 추가
        if (request.getHashtags() != null && !request.getHashtags().isEmpty()) {
            for (String hashtagContent : request.getHashtags()) {
                HashtagEntity hashtag = HashtagEntity.builder()
                        .content(hashtagContent.trim())
                        .member(member)
                        .contentEntity(savedContent)
                        .build();
                savedContent.addHashtag(hashtag);
            }
            contentRepository.save(savedContent);
        }

        log.info("게시물 생성 완료: ID={}", savedContent.getId());
        return convertToResponse(savedContent);
    }

    /**
     * 게시물 상세 조회
     */
    @Transactional(readOnly = true)
    public ContentRes getContent(Long id) {
        log.info("게시물 조회: ID={}", id);

        ContentEntity content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. ID: " + id));

        return convertToResponse(content);
    }

    /**
     * 게시물 목록 검색
     */
    @Transactional(readOnly = true)
    public PageRes<ContentListRes> searchContents(ContentSearchReq request) {
        log.info("게시물 검색: {}", request);

        Sort sort = Sort.by(
                request.getSortDirection().equalsIgnoreCase("ASC")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                request.getSortBy()
        );

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<ContentEntity> contentPage;

        // 해시태그 검색
        if (request.getHashtagContent() != null && !request.getHashtagContent().isEmpty()) {
            contentPage = contentRepository.findByHashtagContent(request.getHashtagContent(), pageable);
        } else {
            contentPage = contentRepository.searchContents(
                    request.getKeyword(),
                    request.getMemberId(),
                    request.getLocation(),
                    pageable
            );
        }

        List<ContentListRes> contentList = contentPage.getContent().stream()
                .map(this::convertToListResponse)
                .collect(Collectors.toList());

        return PageRes.<ContentListRes>builder()
                .content(contentList)
                .page(contentPage.getNumber())
                .size(contentPage.getSize())
                .totalElements(contentPage.getTotalElements())
                .totalPages(contentPage.getTotalPages())
                .last(contentPage.isLast())
                .build();
    }

    /**
     * 게시물 수정
     */
    public ContentRes editContent(Long id, ContentEditReq request) {
        log.info("게시물 수정: ID={}, Request={}", id, request);

        ContentEntity content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물입니다. ID: " + id));

        // 게시물 정보 수정
        if (request.getTitle() != null || request.getContent() != null || request.getLocation() != null) {
            content.updatePost(
                    request.getTitle() != null ? request.getTitle() : content.getTitle(),
                    request.getContent() != null ? request.getContent() : content.getContent(),
                    request.getLocation() != null ? request.getLocation() : content.getLocation()
            );
        }

        // 해시태그 전체 교체
        if (request.getHashtags() != null) {
            // 기존 해시태그 제거
            content.getHashtags().clear();

            // 새 해시태그 추가
            for (String hashtagContent : request.getHashtags()) {
                HashtagEntity hashtag = HashtagEntity.builder()
                        .content(hashtagContent.trim())
                        .member(content.getMember())
                        .contentEntity(content)
                        .build();
                content.addHashtag(hashtag);
            }
        }

        ContentEntity updatedContent = contentRepository.save(content);

        log.info("게시물 수정 완료: ID={}", id);
        return convertToResponse(updatedContent);
    }

    /**
     * 게시물 삭제
     */
    public void deleteContent(Long id) {
        log.info("게시물 삭제: ID={}", id);

        if (!contentRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 게시물입니다. ID: " + id);
        }

        contentRepository.deleteById(id);
        log.info("게시물 삭제 완료: ID={}", id);
    }

    // ========== 변환 메서드 ==========

    private ContentRes convertToResponse(ContentEntity content) {
        List<ContentRes.HashtagInfo> hashtags = content.getHashtags().stream()
                .map(h -> ContentRes.HashtagInfo.builder()
                        .id(h.getId())
                        .content(h.getContent())
                        .build())
                .collect(Collectors.toList());

        return ContentRes.builder()
                .id(content.getId())
                .title(content.getTitle())
                .content(content.getContent())
                .location(content.getLocation())
                .memberId(content.getMember().getId())
                .memberName(content.getMember().getName())
                .regTime(content.getRegTime())
                .updateTime(content.getUpdateTime())
                .hashtags(hashtags)
                .build();
    }

    private ContentListRes convertToListResponse(ContentEntity content) {
        return ContentListRes.builder()
                .id(content.getId())
                .title(content.getTitle())
                .location(content.getLocation())
                .memberId(content.getMember().getId())
                .memberName(content.getMember().getName())
                .regTime(content.getRegTime())
                .updateTime(content.getUpdateTime())
                .hashtagCount(content.getHashtags().size())
                .build();
    }
}