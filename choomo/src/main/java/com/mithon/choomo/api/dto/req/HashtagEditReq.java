package com.mithon.choomo.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "해시태그 수정 요청")
public class HashtagEditReq {

    @NotBlank(message = "해시태그 내용은 필수입니다")
    @Schema(description = "해시태그 내용", example = "맛집추천")
    private String content;
}