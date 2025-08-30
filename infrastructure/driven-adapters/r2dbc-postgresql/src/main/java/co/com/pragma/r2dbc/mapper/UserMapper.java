package co.com.pragma.r2dbc.mapper;

import co.com.pragma.model.user.User;
import co.com.pragma.r2dbc.entity.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return new UserEntity(
                null,
                user.getDocumentNumber(),
                user.getName(),
                user.getLastName(),
                user.getBirthDate(),
                user.getAddress(),
                user.getPhone(),
                user.getEmail(),
                user.getBaseSalary()
        );
    }

    public static User toModel(UserEntity entity) {
        return new User(
                entity.getDocumentNumber(),
                entity.getName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getBaseSalary()
        );
    }
}
