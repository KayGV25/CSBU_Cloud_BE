package com.example.auth.mappers;

import com.example.auth.entity.Department;
import com.example.auth.entity.User;
import com.example.auth.records.DepartmentRequest;
import com.example.auth.records.DepartmentResponse;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentMapper {
    private final UserRepository repository;

    public Department toDepartment(DepartmentRequest request) {
        if (request == null) {
            return null;
        }
        return Department.builder()
                .id(request.id())
                .department_name(request.department_name())
                .head_department(request.head_department())
                .build();
    }

    public DepartmentResponse fromDepartment(Department department) {
        User user = repository.findById(department.getHead_department()).orElse(null);
        String head_name = (user != null) ? user.getFull_name() : "Unknown";

        return new DepartmentResponse(
                department.getId(),
                department.getDepartment_name(),
                department.getHead_department(),
                head_name
        );
    }
}
