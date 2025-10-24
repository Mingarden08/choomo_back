package com.mithon.choomo.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    @Schema(description = "게시물 제목")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "게시물 내용")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Schema(description = "올린사람")
    private Member member;

    @Column(name = "location", nullable = false, length = 200)
    @Schema(description = "장소")
    private String location;

    @LastModifiedDate
    @Column(name = "updateTime", updatable = true)
    @Schema(description = "수정시간")
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "contentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HashtagEntity> hashtags = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (updateTime == null) {
            updateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }

    // 해시태그 추가 헬퍼 메서드
    public void addHashtag(HashtagEntity hashtag) {
        hashtags.add(hashtag);
        hashtag.setContentEntity(this);
    }

    // 해시태그 제거 헬퍼 메서드
    public void removeHashtag(HashtagEntity hashtag) {
        hashtags.remove(hashtag);
        hashtag.setContentEntity(null);
    }

    // 게시물 수정 메서드
    public void updatePost(String title, String content, String location) {
        this.title = title;
        this.content = content;
        this.location = location;
    }
}