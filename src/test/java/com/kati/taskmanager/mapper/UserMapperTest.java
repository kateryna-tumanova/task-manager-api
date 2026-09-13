package com.kati.taskmanager.mapper;

import com.kati.taskmanager.dto.user.UserCreateRequest;
import com.kati.taskmanager.dto.user.UserResponse;
import com.kati.taskmanager.dto.user.UserUpdateRequest;
import com.kati.taskmanager.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toEntity_shouldCreateUserSuccessfully() {

        UserCreateRequest request =
                new UserCreateRequest(
                        "Anna",
                        "anna@example.com"
                );

        User result =
                userMapper.toEntity(request);

        assertEquals(
                request.name(),
                result.getName()
        );

        assertEquals(
                request.email(),
                result.getEmail()
        );
    }

    @Test
    void updateEntity_shouldUpdateUserSuccessfully() {

        UserUpdateRequest request =
                new UserUpdateRequest(
                        "Piotr",
                        "piotr@example.com"
                );

        User user =
                new User(
                        "Anna",
                        "anna@example.com"
                );

        userMapper.updateEntity(request, user);

        assertEquals(
                request.name(),
                user.getName()
        );

        assertEquals(
                request.email(),
                user.getEmail()
        );
    }

    @Test
    void toResponse_shouldReturnUserResponseSuccessfully() {

        User user =
                new User(
                        "Piotr",
                        "piotr@example.com"
                );

        UserResponse result =
                userMapper.toResponse(user);

        assertNull(result.id());

        assertEquals(
                user.getName(),
                result.name()
        );

        assertEquals(
                user.getEmail(),
                result.email()
        );
    }
}
