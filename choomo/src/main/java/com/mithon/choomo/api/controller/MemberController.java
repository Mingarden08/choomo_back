package com.mithon.choomo.api.controller;

import com.mithon.choomo.api.dto.req.MemberSignupRequestDto;
import com.mithon.choomo.api.dto.res.DataResponse;
import com.mithon.choomo.api.dto.res.ResponseCode;
import com.mithon.choomo.api.service.MemberService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<DataResponse<?>> signup(@Valid @RequestBody MemberSignupRequestDto requestDto) {
        log.info(">>>> Signup request: {}", requestDto.toString());
        try {
            boolean result = false;
            result = memberService.signup(requestDto);
            Map<String, Boolean> res = new HashMap<>();
            res.put("success", result);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, res));
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.ALREADY_EXIST_USER_EMAIL, e.getMessage()));
        }
    }

//    @PostMapping("/profile")
//    public ResponseEntity<DataResponse<MemberInfoRes>> profile(@Parameter(hidden = true) Authentication authentication,
//                                                               HttpServletRequest request) {
//
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //또 다른 방법
//        if (authentication == null) {
//            throw new IllegalStateException("Authentication object is null");
//        }
//
//        String id = authentication.getName();
//        MemberInfoRes res = memberService.findByUserId(id);
//        return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, res));
//    }

}
