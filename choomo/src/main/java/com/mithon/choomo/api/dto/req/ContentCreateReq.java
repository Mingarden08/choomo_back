package com.mithon.choomo.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시물 생성 요청")
public class ContentCreateReq {

    @NotBlank(message = "제목은 필수입니다")
    @Schema(description = "게시물 제목", example = "맛집 추천")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Schema(description = "게시물 내용", example = "강남에 있는 맛집입니다")
    private String content;

    @NotBlank(message = "위치는 필수입니다")
    @Schema(description = "장소", example = "서울특별시 강남구")
    private String location;

    @NotNull(message = "회원 ID는 필수입니다")
    @Schema(description = "작성자 ID", example = "1")
    private Long memberId;

    @Schema(description = "해시태그 목록", example = "[\"맛집\", \"강남\", \"데이트\"]")
    private List<String> hashtags;
}
