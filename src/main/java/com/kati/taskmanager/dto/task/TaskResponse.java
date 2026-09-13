package com.kati.taskmanager.dto.task;

import com.kati.taskmanager.dto.project.ProjectResponse;
import com.kati.taskmanager.dto.user.UserResponse;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        String status,
        LocalDate deadline,
        UserResponse assignedUser,
        ProjectResponse project
) {
}
