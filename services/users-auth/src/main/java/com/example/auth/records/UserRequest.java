package com.example.auth.records;

import jakarta.validation.constraints.NotNull;

public record UserRequest(
        String id,
        @NotNull(message = "Your name is required")
        String full_name,
        @NotNull(message = "Your date of birth is required")
        String dob,
        @NotNull(message = "You must choose your department")
        String department_id,
        @NotNull(message = "You must mention how long have you been working")
        Integer yoe,
        Boolean approved,
        String role
) {
}
