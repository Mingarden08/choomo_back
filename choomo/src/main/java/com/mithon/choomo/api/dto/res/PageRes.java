package com.mithon.choomo.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "페이징 응답")
public class PageRes<T> {

    @Schema(description = "데이터 목록")
    private List<T> content;

    @Schema(description = "현재 페이지")
    private int page;

    @Schema(description = "페이지 크기")
    private int size;

    @Schema(description = "전체 요소 수")
    private long totalElements;

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "마지막 페이지 여부")
    private boolean last;
}
