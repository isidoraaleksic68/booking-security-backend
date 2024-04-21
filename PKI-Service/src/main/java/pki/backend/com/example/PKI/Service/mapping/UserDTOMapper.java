package pki.backend.com.example.PKI.Service.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pki.backend.com.example.PKI.Service.dto.UserDTO;
import pki.backend.com.example.PKI.Service.model.User;

@Component
public class UserDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public UserDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static User fromDTOtoUser(UserDTO dto) {
        return modelMapper.map(dto, User.class);
    }

    public static UserDTO fromUserToDTO(User model) {
        return modelMapper.map(model, UserDTO.class);
    }
}