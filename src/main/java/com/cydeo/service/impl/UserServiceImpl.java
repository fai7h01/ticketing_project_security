package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userRepository.findAllByIsDeletedOrderByFirstNameDesc(false).stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDto(userRepository.findByUserNameAndIsDeleted(username, false));
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

//    @Override
//    public void deleteByUserName(String username) {
//        userRepository.deleteByUserName(username);
//    }

    @Override
    public UserDTO update(UserDTO dto) {
        //find current user from db
        User user1 = userRepository.findByUserNameAndIsDeleted(dto.getUserName(), false); // has id
        //map updated user dto to entity
        User convertedDto = userMapper.convertToEntity(dto); // no id
        //set id to the converted object
        convertedDto.setId(user1.getId());
        //save updated user in the db
        userRepository.save(convertedDto);
        return findByUserName(dto.getUserName());
    }

    @Override
    public void delete(String username) {
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        return userRepository.findAllByRoleDescriptionIgnoreCaseAndIsDeleted(role, false).stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user){

        switch (user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                return projectDTOList.isEmpty();
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
                return taskDTOList.isEmpty();
            default:
                return true;
        }
    }

}
