package com.jumpstart.loadshedhub.controller;
<<<<<<< HEAD

=======
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
import com.jumpstart.loadshedhub.dto.*;
import com.jumpstart.loadshedhub.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;
    @PostMapping("/register") public ResponseEntity<Response<AuthResponseDTO>> register(@Valid @RequestBody AuthRequestDTO r) { return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Registered",auth.register(r))); }
    @PostMapping("/login") public Response<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO r) { return Response.success("Logged in",auth.login(r)); }
=======
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController { private final AuthService auth;
 @PostMapping("/register") public ResponseEntity<Response<AuthResponseDTO>> register(@Valid @RequestBody AuthRequestDTO r){ return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Registration successful",auth.register(r))); }
 @PostMapping("/login") public ResponseEntity<Response<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO r){ return ResponseEntity.ok(Response.success("Login successful",auth.login(r))); }
>>>>>>> bce1c30216c5b82041fddfb22f56fd1f90b24ccf
}
