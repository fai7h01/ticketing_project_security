package com.cydeo.mapper;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ModelMapper mapper;

    public TaskMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }


    public Task convertToEntity(TaskDTO dto) {
        return mapper.map(dto, Task.class);
    }

    public TaskDTO convertToDto(Task entity) {
        return mapper.map(entity, TaskDTO.class);
    }

}
