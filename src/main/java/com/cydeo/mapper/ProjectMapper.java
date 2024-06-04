package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final ModelMapper mapper;

    public ProjectMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Project convertToEntity(ProjectDTO dto){
        return mapper.map(dto, Project.class);
    }

    public ProjectDTO convertToDto(Project entity){
        return mapper.map(entity, ProjectDTO.class);
    }
}
