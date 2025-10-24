package com.mithon.demo.api.controller;

import com.mithon.demo.api.dto.req.LoginReq;
import com.mithon.demo.api.dto.res.DataResponse;
import com.mithon.demo.api.dto.res.LoginResultRes;
import com.mithon.demo.api.dto.res.ResponseCode;
import com.mithon.demo.api.service.AuthService;
import com.mithon.demo.api.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse<LoginResultRes>> login(@RequestBody LoginReq req) {
        LoginResultRes res = new LoginResultRes();
        try {
            String token = authService.login(req);
            res.setToken(token);
            return ResponseEntity.ok(DataResponse.of(ResponseCode.SUCCESS, res));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(DataResponse.of(ResponseCode.NOT_MATCHED, res));
        }

    }
}
