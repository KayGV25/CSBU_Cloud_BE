package com.example.auth.services;

import com.example.auth.exception.UserNotFoundException;
import com.example.auth.mappers.DepartmentMapper;
import com.example.auth.records.DepartmentRequest;
import com.example.auth.records.DepartmentResponse;
import com.example.auth.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    public String createDepartment(DepartmentRequest request) {
        var department = repository.save(mapper.toDepartment(request));
        return department.getId();
    }

    public List<DepartmentResponse> getAllDepartment() {
        return repository.findAll()
                .stream()
                .map(mapper::fromDepartment)
                .collect(Collectors.toList());
    }

    public DepartmentResponse getDepartment(String department_id) {
        return repository.findById(department_id)
                .map(mapper::fromDepartment)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("No department found with the provided Id:: %s ", department_id)
                ));
    }
}
