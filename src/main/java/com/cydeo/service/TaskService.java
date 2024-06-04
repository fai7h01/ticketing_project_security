package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {

    List<TaskDTO> listAllTasks();
    TaskDTO findById(Long id);
    void save(TaskDTO dto);
    void delete(Long id);
    void update(TaskDTO dto);
    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);
    void deleteByProject(ProjectDTO dto);
    void completeByProject(ProjectDTO dto);
    List<TaskDTO> listAllByStatusIsNot(Status status);
    List<TaskDTO> listAllByStatusIs(Status status);
    List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee);
}
