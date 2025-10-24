package com.mithon.choomo.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "해시태그 응답")
public class HashtagRes {

    @Schema(description = "해시태그 ID")
    private Long id;

    @Schema(description = "해시태그 내용")
    private String content;

    @Schema(description = "게시물 ID")
    private Long contentId;

    @Schema(description = "게시물 제목")
    private String contentTitle;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "작성자 이름")
    private String memberName;

    @Schema(description = "등록일시")
    private LocalDateTime regTime;
}
