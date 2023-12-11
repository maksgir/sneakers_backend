package com.maksgir.sneakers.service;

import com.maksgir.sneakers.domain.Seller;
import com.maksgir.sneakers.domain.User;
import com.maksgir.sneakers.exception.TokenRefreshException;
import com.maksgir.sneakers.exception.UserAlreadySignedUpException;
import com.maksgir.sneakers.repository.SellerRepository;
import com.maksgir.sneakers.repository.UserRepository;
import com.maksgir.sneakers.security.UserDetailsImpl;
import com.maksgir.sneakers.service.dto.JwtResponse;
import com.maksgir.sneakers.service.dto.LoginRequest;
import com.maksgir.sneakers.service.dto.SignupRequest;
import com.maksgir.sneakers.service.dto.TokenRefreshResponse;
import com.maksgir.sneakers.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse auth(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);


        return new JwtResponse(jwt, userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail());
    }



    public void registerUser(SignupRequest signupRequest) throws UserAlreadySignedUpException {
        checkIfUserAlreadySignedUp(signupRequest);
        createUser(signupRequest);
    }

    private void createUser(SignupRequest sr) {
        User user = new User(sr.firstName(), sr.lastName(), sr.username(), sr.email(), encoder.encode(sr.password()), sr.phone());
        userRepository.save(user);

        if (sr.isSeller()) {
            Seller seller = new Seller(user.getId(), -1);
            sellerRepository.save(seller);
        }

    }

    private void checkIfUserAlreadySignedUp(SignupRequest signUpRequest) throws UserAlreadySignedUpException {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            throw new UserAlreadySignedUpException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new UserAlreadySignedUpException("Email is already in use!");
        }
    }
}
