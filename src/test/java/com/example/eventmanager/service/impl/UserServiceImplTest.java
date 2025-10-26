package com.example.eventmanager.service.impl;

import com.example.eventmanager.dto.request.user.*;
import com.example.eventmanager.dto.response.user.LoginResponse;
import com.example.eventmanager.dto.response.user.TokenResponse;
import com.example.eventmanager.exception.InvalidPasswordException;
import com.example.eventmanager.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.eventmanager.common.ObjectValidator.validate;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@SpringBootTest
class UserServiceImplTest {

    @Autowired UserService userService;

    @Test
    void initiateUser() {
        InitiateUserRequest initiateUser = new InitiateUserRequest(
                "Ko",
                "dhinesh2003@zohomail.in",
                "Dhinesh1@",
                "1234567890"
        );
        validate(initiateUser);
        userService.initiateUser(initiateUser);
    }

    @Test
    void createUser() {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "752518",
                "dhinesh2003@zohomail.in"
        );
        userService.createUser(createUserRequest);
    }

    @Test
    void forgotPassword() {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(
                "dhineshhh1702@gmail.com"
        );
        TokenResponse token = userService.forgotPassword(forgotPasswordRequest);
        log.info("token = {}", token.securityToken);
        log.info("expires in {} minutes", token.expiresInMinutes);
    }

    @Test
    void resetPassword() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(
                "rhhnQVgMk62cEos4mD3cvcDJc7t0vaWOwQ",
                "Dhinesh1@@@@@",
                "Dhinesh1@@@@@"
        );
        log.info("{}", "rhhnQVgMk62cEos4mD3cvcDJc7t0vaWOwQ".length());
        validate(resetPasswordRequest);
        userService.resetPassword(resetPasswordRequest);
    }

    @Test
    void getUser() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(
                "dhineshramadoss10@gmail.com",
                "Dhinesh1@@@@@@@@@@@"
        );

        assertThrows(InvalidPasswordException.class,
                () -> userService.loginUser(loginUserRequest));
    }

    @Test
    void testInitiateUser() {
    }

    @Test
    void testCreateUser() {
    }

    @Test
    void loginUser() {
        LoginUserRequest request = new LoginUserRequest(
                "dhinesh2003@zohomail.in",
                "Dhinesh1@"
        );
        LoginResponse response = userService.loginUser(request);
        log.info("username {}", response.getUserName());
        log.info("token {}", response.getToken());
    }

    @Test
    void getUserById() {
    }
}