package com.csbu.projects.Requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class AddTaskRequest {
    @NotNull
    private String id;
    @NotNull
    private String taskName;
    @NotNull
    private String managerId;
    @NotNull
    private String employeeId;
    @NotNull
    private Date deadline;

    public @NotNull String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public @NotNull String getTaskName() {
        return taskName;
    }

    public @NotNull String getManagerId() {
        return managerId;
    }

    public @NotNull String getEmployeeId() {
        return employeeId;
    }

    public @NotNull Date getDeadline() {
        return deadline;
    }
}
