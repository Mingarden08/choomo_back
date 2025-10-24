package com.mithon.choomo.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시물 상세 응답")
public class ContentRes {

    @Schema(description = "게시물 ID")
    private Long id;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "내용")
    private String content;

    @Schema(description = "위치")
    private String location;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "작성자 이름")
    private String memberName;

    @Schema(description = "등록일시")
    private LocalDateTime regTime;

    @Schema(description = "수정일시")
    private LocalDateTime updateTime;

    @Schema(description = "해시태그 목록")
    private List<HashtagInfo> hashtags;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HashtagInfo {
        private Long id;
        private String content;
    }
}
