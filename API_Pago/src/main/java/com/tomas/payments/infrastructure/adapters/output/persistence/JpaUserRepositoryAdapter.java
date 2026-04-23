package com.tomas.payments.infrastructure.adapters.output.persistence;

import java.util.Optional;

import com.tomas.payments.application.ports.output.UserRepositoryPort;
import com.tomas.payments.domain.model.User;

public class JpaUserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;

    public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springDataUserRepository.findByUsername(username)
                .map(UserPersistenceMapper::toDomain);
    }

}
