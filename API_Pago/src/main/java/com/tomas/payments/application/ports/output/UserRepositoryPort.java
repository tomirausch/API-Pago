package com.tomas.payments.application.ports.output;

import java.util.Optional;

import com.tomas.payments.domain.model.User;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
}
