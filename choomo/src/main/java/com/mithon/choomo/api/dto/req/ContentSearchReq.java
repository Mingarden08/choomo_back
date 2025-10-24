package com.mithon.choomo.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시물 검색 요청")
public class ContentSearchReq {

    @Schema(description = "검색 키워드 (제목, 내용 검색)")
    private String keyword;

    @Schema(description = "작성자 ID")
    private Long memberId;

    @Schema(description = "위치")
    private String location;

    @Schema(description = "해시태그 내용")
    private String hashtagContent;

    @Builder.Default
    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private int page = 0;

    @Builder.Default
    @Schema(description = "페이지 크기", example = "20")
    private int size = 20;

    @Builder.Default
    @Schema(description = "정렬 기준", example = "regTime")
    private String sortBy = "regTime";

    @Builder.Default
    @Schema(description = "정렬 방향 (ASC/DESC)", example = "DESC")
    private String sortDirection = "DESC";
}
