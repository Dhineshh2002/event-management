package com.example.eventmanager.service.impl;

import com.example.eventmanager.dto.request.user.GetUserRequest;
import com.example.eventmanager.dto.request.user.OtpRequest;
import com.example.eventmanager.dto.request.user.SetNewPasswordRequest;
import com.example.eventmanager.dto.request.user.UserRequest;
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
        UserRequest initiateUser = new UserRequest(
                "Ramadoss",
                "dhineshramadoss10@gmail.com",
                "Dhinesh1@",
                "1234567890"
        );
        validate(initiateUser);
        userService.initiateUser(initiateUser);
    }

    @Test
    void createUser() {
        OtpRequest otpRequest = new OtpRequest(
                "720192",
                "dhineshramadoss10@gmail.com"
        );
        userService.createUser(otpRequest);
    }

    @Test
    void forgotPassword() {
        TokenResponse token = userService.forgotPassword("dhineshhh1702@gmail.com");
        log.info("token = {}", token.securityToken);
        log.info("expires in {} minutes", token.expiresInMinutes);
    }

    @Test
    void resetPassword() {
        SetNewPasswordRequest setNewPasswordRequest = new SetNewPasswordRequest(
                "rhhnQVgMk62cEos4mD3cvcDJc7t0vaWOwQ",
                "Dhinesh1@@@@@",
                "Dhinesh1@@@@@"
        );
        log.info("{}", "rhhnQVgMk62cEos4mD3cvcDJc7t0vaWOwQ".length());
        validate(setNewPasswordRequest);
        userService.resetPassword(setNewPasswordRequest);
    }

    @Test
    void getUser() {
        GetUserRequest getUserRequest = new GetUserRequest(
                "dhineshramadoss10@gmail.com",
                "Dhinesh1@@@@@@@@@@@"
        );

        assertThrows(InvalidPasswordException.class,
                () -> userService.getUser(getUserRequest));
    }
}