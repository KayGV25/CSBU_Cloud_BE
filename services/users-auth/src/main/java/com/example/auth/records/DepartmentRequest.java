package com.example.auth.records;

import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        String id,
        @NotNull(message = "Name for department is required")
        String department_name,
        @NotNull(message = "Head of department is required")
        String head_department
) {
}
