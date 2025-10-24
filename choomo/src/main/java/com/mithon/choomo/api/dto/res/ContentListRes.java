package com.mithon.choomo.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시물 목록 응답")
public class ContentListRes {

    @Schema(description = "게시물 ID")
    private Long id;

    @Schema(description = "제목")
    private String title;

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

    @Schema(description = "해시태그 개수")
    private int hashtagCount;
}

