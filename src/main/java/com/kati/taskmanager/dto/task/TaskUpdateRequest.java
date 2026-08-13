package com.kati.taskmanager.dto.task;

import java.time.LocalDate;

public record TaskUpdateRequest(
        String title,
        String description,
        String status,
        LocalDate deadline,
        Long assignedUserId,
        Long projectId
) {
}
