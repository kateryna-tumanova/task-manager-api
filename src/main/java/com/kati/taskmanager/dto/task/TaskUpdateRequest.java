package com.kati.taskmanager.dto.task;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskUpdateRequest(
        @NotBlank
        @Size(max = 100)
        String title,

        @NotBlank
        @Size(max = 1000)
        String description,

        @NotBlank
        @Size(max = 100)
        String status,

        @NotNull
        @FutureOrPresent
        LocalDate deadline,

        @NotNull
        @Positive
        Long assignedUserId,

        @NotNull
        @Positive
        Long projectId
) {
}
