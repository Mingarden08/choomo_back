package com.mithon.choomo.api.controller;

import com.mithon.choomo.api.dto.req.ContentCreateReq;
import com.mithon.choomo.api.dto.req.ContentEditReq;
import com.mithon.choomo.api.dto.req.ContentSearchReq;
import com.mithon.choomo.api.dto.res.*;
import com.mithon.choomo.api.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@Tag(name = "Content", description = "게시물 API")
public class ContentController {

    private final ContentService contentService;

    /**
     * 게시물 생성
     * POST /api/content/create
     */
    @PostMapping("/create")
    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성합니다")
    public ResponseEntity<DataResponse<ContentRes>> createContent(
            @Valid @RequestBody ContentCreateReq request) {
        try {
            ContentRes response = contentService.createContent(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(DataResponse.of(ResponseCode.SUCCESS, response));
        } catch (IllegalArgumentException e) {
            log.error("게시물 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("게시물 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }

    /**
     * 게시물 조회 및 목록
     * GET /api/content/search
     */
    @GetMapping("/search")
    @Operation(summary = "게시물 조회 및 목록", description = "게시물 상세 조회 또는 목록을 조회합니다")
    public ResponseEntity<DataResponse<?>> searchContent(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String hashtagContent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "regTime") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        try {
            // id가 제공된 경우 상세 조회
            if (id != null) {
                ContentRes response = contentService.getContent(id);
                return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, response));
            }

            // 목록 조회
            ContentSearchReq searchRequest = ContentSearchReq.builder()
                    .keyword(keyword)
                    .memberId(memberId)
                    .location(location)
                    .hashtagContent(hashtagContent)
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();

            PageRes<ContentListRes> response = contentService.searchContents(searchRequest);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, response));
        } catch (IllegalArgumentException e) {
            log.error("게시물 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("게시물 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }

    /**
     * 게시물 수정M73046497ac!
     *
     * PATCH /api/content/edit
     */
    @PatchMapping("/edit")
    @Operation(summary = "게시물 수정", description = "게시물을 수정합니다")
    public ResponseEntity<DataResponse<ContentRes>> editContent(
            @RequestParam Long id,
            @Valid @RequestBody ContentEditReq request) {
        try {
            ContentRes response = contentService.editContent(id, request);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, response));
        } catch (IllegalArgumentException e) {
            log.error("게시물 수정 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("게시물 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }

    /**
     * 게시물 삭제
     * DELETE /api/content/delete
     */
    @DeleteMapping("/delete")
    @Operation(summary = "게시물 삭제", description = "게시물을 삭제합니다")
    public ResponseEntity<DataResponse<Void>> deleteContent(@RequestParam Long id) {
        try {
            contentService.deleteContent(id);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, null));
        } catch (IllegalArgumentException e) {
            log.error("게시물 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("게시물 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }
}