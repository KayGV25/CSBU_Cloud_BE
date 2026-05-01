package com.csbu.projects.controller;

import com.csbu.projects.Dto.PageResponse;
import com.csbu.projects.Dto.TaskDto;
import com.csbu.projects.Models.Task;
import com.csbu.projects.Requests.AddTaskRequest;
import com.csbu.projects.Services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    @Autowired
    private TaskService taskService;

//    @GetMapping
//    public String testTask() {
//        return "Task đây nha chú em - Port 8070 - Good luck";
//    }
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody @Valid AddTaskRequest request)
    {
        Task task = new Task();
        task.setId(request.getId());
        task.setTaskName(request.getTaskName());
        task.setManagerId(request.getManagerId());
        task.setEmployeeId(request.getEmployeeId());
        task.setDeadline(request.getDeadline());
        task.setStatus(false);
        taskService.createTask(task);
        return  ResponseEntity.status(HttpStatus.CREATED).body("Task has been created!");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatusById(@PathVariable(name = "id") String id){
        taskService.updateTaskStatus(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Task's status has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable(name="id") String id){
        taskService.deleteTaskById(id);
        return  ResponseEntity.status(HttpStatus.OK).body("Task has been deleted!");
    }

    @GetMapping("/{employee_id}")
    public ResponseEntity<PageResponse<TaskDto>> getTaskByEmployeeId(
            @PathVariable(name="employee_id") String employee_id,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ){
        Page<TaskDto> task=taskService.getTasksByEmployeeId(employee_id, page, size);
        PageResponse<TaskDto> pageResponse = new PageResponse<>(
                task.getNumber() + 1,
                task.getTotalPages(),
                task.getSize(),
                (int) task.getTotalElements(),
                task.getContent()
        );
        return ResponseEntity.ok(pageResponse);
    }
}
