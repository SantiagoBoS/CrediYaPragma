package co.com.pragma.api.user.mapper;

import co.com.pragma.api.user.dto.UserDTO;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.user.User;

public class UserMapper {
    UserMapper() {
        throw new UnsupportedOperationException(String.valueOf(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage()));
    }

    public static User toEntity(UserDTO userDto) {
        if (userDto == null) {
            return null;
        }

        return User.builder()
                .documentNumber(userDto.getDocumentNumber())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .birthDate(userDto.getBirthDate())
                .address(userDto.getAddress())
                .phone(userDto.getPhone())
                .email(userDto.getEmail())
                .baseSalary(userDto.getBaseSalary())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .build();
    }

    public static UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .documentNumber(user.getDocumentNumber())
                .name(user.getName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .baseSalary(user.getBaseSalary())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
