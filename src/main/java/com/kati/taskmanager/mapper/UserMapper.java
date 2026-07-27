package com.kati.taskmanager.mapper;

import com.kati.taskmanager.dto.user.UserCreateRequest;
import com.kati.taskmanager.dto.user.UserResponse;
import com.kati.taskmanager.dto.user.UserUpdateRequest;
import com.kati.taskmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        return new User(
                request.name(),
                request.email()
        );
    }

    public void updateEntity(UserUpdateRequest request, User user) {
        user.setName(request.name());
        user.setEmail(request.email());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
