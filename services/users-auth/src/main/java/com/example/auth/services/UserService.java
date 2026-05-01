package com.example.auth.services;

import com.example.auth.entity.User;
import com.example.auth.exception.UserNotFoundException;
import com.example.auth.mappers.UserMapper;
import com.example.auth.records.UserRequest;
import com.example.auth.records.UserResponse;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public String createUser(UserRequest request) {
        var user = repository.save(mapper.toUser(request));
        return user.getId();
    }

    public List<UserResponse> getAllUser() {
        return repository.findAll()
                .stream()
                .map(mapper::fromUser)
                .collect(Collectors.toList());
    }

    public UserResponse getUser(String user_id) {
        return repository.findById(user_id)
                .map(mapper::fromUser)
                .orElseThrow(() -> new UserNotFoundException(
                    String.format("No user found with the provided Id:: %s ", user_id)
                ));
    }

    public String updateUser(String userId, UserRequest updatedUser) {
        // Tìm User hiện tại bằng ID
        Optional<User> userOptional = repository.findById(userId);

        // Nếu User không tồn tại, trả về null hoặc có thể ném exception
        if (!userOptional.isPresent()) {
            return null; // Hoặc có thể ném một exception như NotFoundException
        }

        User user = userOptional.get();

        // Cập nhật các trường thông tin (ví dụ: fullname, dob, departmentId, ...)
        if (updatedUser.full_name() != null) {
            user.setFull_name(updatedUser.full_name());
        }
        if (updatedUser.dob() != null) {
            user.setDob(updatedUser.dob());
        }
        if (updatedUser.department_id() != null) {
            user.setDepartment_id(updatedUser.department_id());
        }
        if (updatedUser.yoe() != null) {
            user.setYoe(updatedUser.yoe());
        }
        if (updatedUser.approved() != null) {
            user.setApproved(updatedUser.approved());
        }
        if (updatedUser.role() != null) {
            user.setRole(updatedUser.role());
        }
        repository.save(user);
        // Lưu User đã cập nhật vào MongoDB
        return "Updated Successfully"; // Phương thức save sẽ cập nhật nếu đã tồn tại
    }
}
