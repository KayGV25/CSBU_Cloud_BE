package com.example.auth.mappers;

import com.example.auth.entity.Department;
import com.example.auth.entity.User;
import com.example.auth.records.UserRequest;
import com.example.auth.records.UserResponse;
import com.example.auth.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final DepartmentRepository repository;

    public User toUser(UserRequest request) {
        return User.builder()
                .id(request.id())
                .full_name(request.full_name())
                .dob(request.dob())
                .department_id(request.department_id())
                .yoe(request.yoe())
                .build();
    }

    public UserResponse fromUser(User user) {
        Department department = repository.findById(user.getDepartment_id()).orElse(null);
        // Default nếu không tìm thấy department
        String departmentName = (department != null) ? department.getDepartment_name() : "Unknown";

        return new UserResponse(
                user.getId(),
                user.getFull_name(),
                user.getDob(),
                user.getDepartment_id(),
                departmentName,
                user.getYoe()
        );
    }
}
