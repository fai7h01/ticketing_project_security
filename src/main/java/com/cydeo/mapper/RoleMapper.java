package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    private final ModelMapper mapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.mapper = modelMapper;
    }

    public Role convertToEntity(RoleDTO roleDTO){
        return mapper.map(roleDTO, Role.class);
    }

    public RoleDTO convertToDto(Role role){
        return mapper.map(role, RoleDTO.class);
    }


}
