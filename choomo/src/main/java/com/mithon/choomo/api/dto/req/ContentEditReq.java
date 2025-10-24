package com.mithon.choomo.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시물 수정 요청")
public class ContentEditReq {

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 내용")
    private String content;

    @Schema(description = "장소")
    private String location;

    @Schema(description = "해시태그 목록 (전체 교체)")
    private List<String> hashtags;
}

