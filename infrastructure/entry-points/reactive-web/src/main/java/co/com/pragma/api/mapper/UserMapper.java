package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.UserRequestDTO;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.constants.AppMessages;

public class UserMapper {
    private UserMapper() {
        throw new UnsupportedOperationException(String.valueOf(AppMessages.CLASS_SHOULD_NOT_BE_INSTANTIATED));
    }

    public static User toEntity(UserRequestDTO userDto) {
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
                .build();
    }

    public static UserRequestDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserRequestDTO.builder()
                .documentNumber(user.getDocumentNumber())
                .name(user.getName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .baseSalary(user.getBaseSalary())
                .build();
    }
}
