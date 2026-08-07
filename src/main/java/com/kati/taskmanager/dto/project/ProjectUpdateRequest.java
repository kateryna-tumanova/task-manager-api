package com.kati.taskmanager.dto.project;

import java.time.LocalDate;

public record ProjectUpdateRequest(
        String name,
        String status,
        LocalDate deadline
) {
}
