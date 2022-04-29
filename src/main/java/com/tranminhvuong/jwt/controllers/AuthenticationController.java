package com.tranminhvuong.jwt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tranminhvuong.jwt.common.ERole;
import com.tranminhvuong.jwt.common.TokenProvider;
import com.tranminhvuong.jwt.dto.ResponseObject;
import com.tranminhvuong.jwt.entities.Role;
import com.tranminhvuong.jwt.entities.User;
import com.tranminhvuong.jwt.payloads.LoginRequest;
import com.tranminhvuong.jwt.payloads.LoginResponse;
import com.tranminhvuong.jwt.payloads.SignupRequest;
import com.tranminhvuong.jwt.repositories.IRoleRepository;
import com.tranminhvuong.jwt.repositories.IUserRepository;
import com.tranminhvuong.jwt.services.UserPrincipal;

/**
 * CrossOrigin bảo mật request gửi đến domain khác
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Đánh dấu object với @Valid để validator tự động kiểm tra object đó có hợp lệ hay không
     */
    /**
     * [POST] /api/v1/auth/signin
     * @param loginRequest
     * @return ResponseEntity
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        System.out.println("OK");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new LoginResponse(jwt, userPrincipal.getUsername(), userPrincipal.getEmail(), roles));
    }

    /**
     * [POST] /api/v1/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if(iUserRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject(400, "Username is already taken!", ""));
        }

        System.out.println(signupRequest.getEmail());
        System.out.println(iUserRepository.existsByEmail("user@gmail.com"));
        if(iUserRepository.existsByEmail(signupRequest.getEmail())) {
        	return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject(400, "Email is already taken!", ""));
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
        		passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        // check lại
        if(strRoles == null) {
            Role userRole = iRoleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = iRoleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

                        roles.add(adminRole);

                        break;

                    case "moderator":
                        Role moderatorRole = iRoleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

                        roles.add(moderatorRole);

                        break;

                    default:
                        Role userRole = iRoleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));

                        roles.add(userRole);

                        break;
                }
            });
        }

        user.setRoles(roles);
        iUserRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseObject(201, "User registered successfully!", ""));
    }

}
