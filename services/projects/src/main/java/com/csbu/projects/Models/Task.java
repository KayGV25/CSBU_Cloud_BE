package com.csbu.projects.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    private String id;

    @Column(name="task_name", nullable = false)
    private String taskName;


//    private Employee manager;
    @Column(name="manager_id", nullable = false)
    private String managerId;

//    private Employee employee;
    @Column(name="employee_id", nullable = false)
    private String employeeId;

    @Temporal(TemporalType.DATE)
    @Column(name = "deadline" ,nullable = false)
    private Date deadline;

    @Column(name = "status", nullable = false)
    private boolean status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
