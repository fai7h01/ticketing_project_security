package com.cydeo.mapper;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper mapper;

    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public User convertToEntity(UserDTO userDTO){
        return mapper.map(userDTO, User.class);
    }

    public UserDTO convertToDto(User user){
        return mapper.map(user, UserDTO.class);
    }

}
