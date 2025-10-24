package com.mithon.choomo.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "해시태그 검색 요청")
public class HashtagSearchReq {

    @Schema(description = "해시태그 내용 검색")
    private String content;

    @Schema(description = "게시물 ID")
    private Long contentId;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Builder.Default
    @Schema(description = "페이지 번호", example = "0")
    private int page = 0;

    @Builder.Default
    @Schema(description = "페이지 크기", example = "20")
    private int size = 20;
}
