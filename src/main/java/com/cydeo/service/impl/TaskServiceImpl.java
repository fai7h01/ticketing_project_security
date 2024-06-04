package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        return foundTask.map(taskMapper::convertToDto).orElse(null);
    }

    @Override
    public void save(TaskDTO dto) {
        dto.setAssignedDate(LocalDate.now());
        dto.setStatus(Status.OPEN);
        taskRepository.save(taskMapper.convertToEntity(dto));
    }

    @Override
    public void delete(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> foundTask = taskRepository.findById(dto.getId());
        Task convertedDto = taskMapper.convertToEntity(dto);
        if (foundTask.isPresent()){
            convertedDto.setStatus(dto.getStatus() == null ? foundTask.get().getStatus() : dto.getStatus());
            convertedDto.setAssignedDate(foundTask.get().getAssignedDate());
            taskRepository.save(convertedDto);
        }
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO dto) {
        Project project = projectMapper.convertToEntity(dto);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO dto) {
        Project project = projectMapper.convertToEntity(dto);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.stream().map(taskMapper::convertToDto).forEach(taskDTO -> {
            taskDTO.setStatus(Status.COMPLETE);
            update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> listAllByStatusIsNot(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.
                findAllByStatusIsNotAndAssignedEmployee(status, userMapper.convertToEntity(loggedInUser));
        return tasks.stream()
                .map(taskMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllByStatusIs(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.
                findAllByStatusAndAssignedEmployee(status, userMapper.convertToEntity(loggedInUser));
        return tasks.stream()
                .map(taskMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {
        List<Task> list = taskRepository
                .findAllByStatusIsNotAndAssignedEmployee(Status.COMPLETE, userMapper.convertToEntity(assignedEmployee));
        return list.stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
}
