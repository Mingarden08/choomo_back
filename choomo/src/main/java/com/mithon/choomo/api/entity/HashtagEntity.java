package com.mithon.choomo.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hashtag")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashtagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content", nullable = false, length = 100)
    @Schema(description = "해시태그 내용")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Schema(description = "만든사람")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    @Schema(description = "게시물")
    private ContentEntity contentEntity;

    // 해시태그 내용 수정 메서드
    public void updateContent(String content) {
        this.content = content;
    }
}