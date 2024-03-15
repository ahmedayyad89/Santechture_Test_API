package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.user.UserDto;
import com.santechture.api.entity.Token;
import com.santechture.api.entity.User;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.TokenRepository;
import com.santechture.api.repository.UserRepository;
import com.santechture.api.validation.AddUserRequest;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;


    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }


    public ResponseEntity<GeneralResponse> list(Pageable pageable) {
        return new GeneralResponse().response(userRepository.findAll(pageable));
    }

    public ResponseEntity<GeneralResponse> addNewUser(AddUserRequest request, String jwt) throws BusinessExceptions {

        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new BusinessExceptions("username.exist");
        } else if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessExceptions("email.exist");
        }

        User user = new User(request.getUsername(), request.getEmail());
        userRepository.save(user);
        logoutToken(jwt);
        return new GeneralResponse().response(new UserDto(user));
    }

    private void logoutToken(String jwt) {
        Optional<Token> optionalToken = tokenRepository.findByToken(jwt);
        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            token.setIsLoggedOut(true);
            tokenRepository.save(token);
        }
    }

}
