package com.mithon.choomo.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "해시태그 생성 요청")
public class HashtagCreateReq {

    @NotBlank(message = "해시태그 내용은 필수입니다")
    @Schema(description = "해시태그 내용", example = "맛집")
    private String content;

    @NotNull(message = "게시물 ID는 필수입니다")
    @Schema(description = "게시물 ID", example = "1")
    private Long contentId;

    @NotNull(message = "회원 ID는 필수입니다")
    @Schema(description = "작성자 ID", example = "1")
    private Long memberId;
}
