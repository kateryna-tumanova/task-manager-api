package com.kati.taskmanager.dto.user;

public record UserCreateRequest(
        String name,
        String email
) {
}
