package com.tomas.payments.infrastructure.adapters.output.persistence;

public class UserPersistenceMapper {
    public static UserEntity toEntity(com.tomas.payments.domain.model.User user) {
        if (user == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        return entity;
    }

    public static com.tomas.payments.domain.model.User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new com.tomas.payments.domain.model.User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole()
        );
    }
}
