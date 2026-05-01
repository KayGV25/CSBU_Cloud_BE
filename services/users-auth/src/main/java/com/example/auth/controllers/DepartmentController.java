package com.example.auth.controllers;

import com.example.auth.records.DepartmentRequest;
import com.example.auth.records.DepartmentResponse;
import com.example.auth.services.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService services;

    @PostMapping
    public ResponseEntity<String> newDepartment(
            @RequestBody @Valid DepartmentRequest request
    ) {
        return ResponseEntity.ok(services.createDepartment(request));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartment() {
        return ResponseEntity.ok(services.getAllDepartment());
    }

    @GetMapping("/{department_id}")
    public ResponseEntity<DepartmentResponse> getDepartment(
            @PathVariable("department_id") String department_id
    ) {
        return ResponseEntity.ok(services.getDepartment(department_id));
    }
}
