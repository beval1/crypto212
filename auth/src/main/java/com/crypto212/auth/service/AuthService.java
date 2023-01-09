package com.crypto212.auth.service;

import com.crypto212.auth.exception.RoleNotFoundException;
import com.crypto212.auth.exception.UserAlreadyExistsException;
import com.crypto212.auth.model.dto.payload.SigninDTO;
import com.crypto212.auth.model.dto.payload.SignupDTO;
import com.crypto212.auth.model.entity.RoleEntity;
import com.crypto212.auth.model.entity.RoleEnum;
import com.crypto212.auth.model.entity.UserEntity;
import com.crypto212.auth.repository.RoleRepository;
import com.crypto212.auth.repository.UserRepository;
import com.crypto212.auth.security.JwtTokenProvider;
import com.crypto212.clients.auth.JwtTokenClaims;
import com.crypto212.shared.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class AuthService {
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String signInUser(SigninDTO signinDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinDto.getUsernameOrEmail(), signinDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateJwtToken(authentication);
    }

    public void signUpUser(SignupDTO signupDto) {
        //check if username already exist
        if (userRepository.getUserByUsername(signupDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST,
                    "User with such username already exists!");
        }

        if (userRepository.getUserByEmail(signupDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(HttpStatus.BAD_REQUEST,
                    "User with such email already exists!");
        }

        RoleEntity role = roleRepository.findByRoleName(RoleEnum.USER).orElseThrow(()
                -> new RoleNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "No Default User Role in the database"));

        //create new user
        UserEntity user = modelMapper.map(signupDto, UserEntity.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRoles(Set.of(role));
        user.setEnabled(true);
        user.setLocked(false);
        user.setAccountExpired(false);
        user.setCredentialsExpired(false);

        userRepository.createUser(user.getUsername(), user.getEmail(), user.getPassword(), user.isEnabled(),
                user.isLocked(), user.isAccountExpired(),  user.isCredentialsExpired(), user.getRoles());
    }

    public JwtTokenClaims getTokenClaims(String token) {
        if (!jwtTokenProvider.validateJwtToken(token)){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Jwt Token");
        }

        return new JwtTokenClaims(jwtTokenProvider.getRolesFromJwtToken(token),
                jwtTokenProvider.getUserIdFromJwtToken(token));
    }
}
