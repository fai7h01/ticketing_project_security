package com.cydeo.converter;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.service.ProjectService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ProjectDTOConverter implements Converter<String, ProjectDTO> {

    private final ProjectService projectService;

    public ProjectDTOConverter(@Lazy ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public ProjectDTO convert(String source) {
        if (source == null || source.isEmpty()){
            return null;
        }
        return projectService.getByProjectCode(source);
    }
}
