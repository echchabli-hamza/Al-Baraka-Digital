package com.AlBaraka.demo.controller;

import com.AlBaraka.demo.dto.AuthRequest;
import com.AlBaraka.demo.dto.AuthResponse;
import com.AlBaraka.demo.dto.RegisterRequest;
import com.AlBaraka.demo.entity.User;
import com.AlBaraka.demo.entity.enums.UserRole;
import com.AlBaraka.demo.repository.UserRepo;
import com.AlBaraka.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;





    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );


            UserDetails userDetails = (UserDetails) authentication.getPrincipal();


            String token = jwtService.generateToken(userDetails);

            String role = userDetails.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            return ResponseEntity.ok(
                    new AuthResponse(token, "new message test",  userDetails.getUsername(),role  )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null,  "","Invalid email or password", null));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "", "User not found", null));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(null, "","Login failed: " + e.getMessage(), null));
        }
    }


    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth controller is working!");
    }
}