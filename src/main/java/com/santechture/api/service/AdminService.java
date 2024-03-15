package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.TokenDto;
import com.santechture.api.entity.Admin;
import com.santechture.api.entity.Token;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.AdminRepository;
import com.santechture.api.repository.TokenRepository;
import com.santechture.api.utils.JwtUtil;
import com.santechture.api.validation.LoginRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final TokenRepository tokenRepository;

    private final AdminRepository adminRepository;


    public AdminService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AdminRepository adminRepository,
            TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<GeneralResponse> login(LoginRequest request) throws BusinessExceptions {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            Admin admin = adminRepository.findByUsernameIgnoreCase(authentication.getName());
            String jwt = jwtUtil.createToken(admin);
            logoutAllTokens(admin.getAdminId());
            saveNewToken(jwt, admin);
            return new GeneralResponse().response(new TokenDto(jwt));

        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponse().response(500, e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void logoutAllTokens(Integer adminId) {
        List<Token> activeTokensByAdmin = tokenRepository.findAllTokenByAdminId(adminId);
        for (Token token : activeTokensByAdmin) {
            token.setIsLoggedOut(true);
        }
        tokenRepository.saveAll(activeTokensByAdmin);
    }

    private void saveNewToken(String jwt, Admin admin) {
        Token newToken = new Token();
        newToken.setToken(jwt);
        newToken.setIsLoggedOut(false);
        newToken.setAdmin(admin);
        tokenRepository.save(newToken);
    }

}
