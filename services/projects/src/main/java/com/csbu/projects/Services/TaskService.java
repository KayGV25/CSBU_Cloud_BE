package com.csbu.projects.Services;

import com.csbu.projects.Dto.TaskDto;
import com.csbu.projects.Models.Task;
import com.csbu.projects.Repositories.TaskRepository;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Page<TaskDto> getTasksByEmployeeId(String employee_id, int page, int size) {
        try {
            Sort sort = Sort.by("deadline").descending();
            Pageable pageable = PageRequest.of(page - 1, size, sort);

            Page<Task> taskPage = taskRepository.findByEmployeeId(employee_id, pageable);

            List<TaskDto> taskDtos = taskPage.stream()
                    .map(task -> new TaskDto(
                            task.getId(),
                            task.getTaskName(),
                            task.getManagerId(),
                            task.getEmployeeId(),
                            task.getDeadline(),
                            task.getStatus()
                    ))
                    .collect(Collectors.toList());  // Collecting into a list

            // Returning the mapped data in a PageImpl object
            return new PageImpl<>(taskDtos, pageable, taskPage.getTotalElements());

        } catch (Exception e) {
            throw new TransactionException("Error getting status", e);
        }
    }

    public void createTask(Task task){
        try{
            taskRepository.save(task);
        }catch (Exception e){
            throw new TransactionException("Error creating status",e);
        }
    }
    public void updateTaskStatus(String id){
        try{
            taskRepository.updateStatusById(id);
        }catch (Exception e){
            throw new TransactionException("Error updating status",e);
        }
    }

    public void deleteTaskById(String id){
        try{
            taskRepository.deleteById(id);
        }catch (Exception e){
            throw new TransactionException("Error deleting status",e);
        }
    }
}

