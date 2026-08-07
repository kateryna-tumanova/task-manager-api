package com.kati.taskmanager.dto.project;

import java.time.LocalDate;

public record ProjectResponse(
        Long id,
        String name,
        String status,
        LocalDate deadline
) {
}
