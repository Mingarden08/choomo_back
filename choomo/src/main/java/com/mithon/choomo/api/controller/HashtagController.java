package com.mithon.choomo.api.controller;

import com.mithon.choomo.api.dto.req.HashtagCreateReq;
import com.mithon.choomo.api.dto.req.HashtagEditReq;
import com.mithon.choomo.api.dto.req.HashtagSearchReq;
import com.mithon.choomo.api.dto.res.DataResponse;
import com.mithon.choomo.api.dto.res.HashtagRes;
import com.mithon.choomo.api.dto.res.PageRes;
import com.mithon.choomo.api.dto.res.ResponseCode;
import com.mithon.choomo.api.service.HashtagService;
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
@RequestMapping("/api/tag")
@RequiredArgsConstructor
@Tag(name = "Hashtag", description = "해시태그 API")
public class HashtagController {

    private final HashtagService hashtagService;

    /**
     * 해시태그 생성
     * POST /api/tag/create
     */
    @PostMapping("/create")
    @Operation(summary = "해시태그 생성", description = "새로운 해시태그를 생성합니다")
    public ResponseEntity<DataResponse<HashtagRes>> createHashtag(
            @Valid @RequestBody HashtagCreateReq request) {
        try {
            HashtagRes response = hashtagService.createHashtag(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(DataResponse.of(ResponseCode.SUCCESS, response));
        } catch (IllegalArgumentException e) {
            log.error("해시태그 생성 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("해시태그 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }

    /**
     * 해시태그 조회
     * GET /api/tag/search
     */
    @GetMapping("/search")
    @Operation(summary = "해시태그 조회", description = "해시태그 상세 조회 또는 목록을 조회합니다")
    public ResponseEntity<DataResponse<?>> searchHashtag(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) Long contentId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            // id가 제공된 경우 상세 조회
            if (id != null) {
                HashtagRes response = hashtagService.getHashtag(id);
                return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, response));
            }

            // 목록 조회
            HashtagSearchReq searchRequest = HashtagSearchReq.builder()
                    .content(content)
                    .contentId(contentId)
                    .memberId(memberId)
                    .page(page)
                    .size(size)
                    .build();

            PageRes<HashtagRes> response = hashtagService.searchHashtags(searchRequest);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, response));
        } catch (IllegalArgumentException e) {
            log.error("해시태그 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("해시태그 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }

    /**
     * 해시태그 수정
     * PATCH /api/tag/edit
     */
    @PatchMapping("/edit")
    @Operation(summary = "해시태그 수정", description = "해시태그를 수정합니다")
    public ResponseEntity<DataResponse<HashtagRes>> editHashtag(
            @RequestParam Long id,
            @Valid @RequestBody HashtagEditReq request) {
        try {
            HashtagRes response = hashtagService.editHashtag(id, request);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, response));
        } catch (IllegalArgumentException e) {
            log.error("해시태그 수정 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("해시태그 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }

    /**
     * 해시태그 삭제
     * DELETE /api/tag/delete
     */
    @DeleteMapping("/delete")
    @Operation(summary = "해시태그 삭제", description = "해시태그를 삭제합니다")
    public ResponseEntity<DataResponse<Void>> deleteHashtag(@RequestParam Long id) {
        try {
            hashtagService.deleteHashtag(id);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, null));
        } catch (IllegalArgumentException e) {
            log.error("해시태그 삭제 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        } catch (Exception e) {
            log.error("해시태그 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(DataResponse.of(ResponseCode.NOT_VALID));
        }
    }
}