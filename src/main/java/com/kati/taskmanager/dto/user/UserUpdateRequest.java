package com.kati.taskmanager.dto.user;

public record UserUpdateRequest(
        String name,
        String email
) {
}
