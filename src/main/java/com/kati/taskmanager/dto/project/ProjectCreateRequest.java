package com.kati.taskmanager.dto.project;

import java.time.LocalDate;

public record ProjectCreateRequest(
        String name,
        String status,
        LocalDate deadline
) {
}
