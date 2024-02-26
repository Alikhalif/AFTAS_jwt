package com.youcode.myaftas.Controller;

import com.youcode.myaftas.dto.MemberDto;
import com.youcode.myaftas.dto.authDTO.AuthenticationRequest;
import com.youcode.myaftas.dto.authDTO.AuthenticationResponse;
import com.youcode.myaftas.dto.authDTO.RegisterRequest;
import com.youcode.myaftas.dto.responseDTO.MemberRespDto;
import com.youcode.myaftas.entities.User;
import com.youcode.myaftas.service.ImplService.AuthenticationService;
import com.youcode.myaftas.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    @Autowired
    private AuthenticationService memberService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("ok");
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        System.out.println("login ok !!!");
        return ResponseEntity.ok(authService.authenticate(request));
    }


}
