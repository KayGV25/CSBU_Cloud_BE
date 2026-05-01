package com.example.auth.records;

public record UserResponse(
        String id,
        String full_name,
        String dob,
        String department_id,
        String department_name,
        Integer yoe
) {}
