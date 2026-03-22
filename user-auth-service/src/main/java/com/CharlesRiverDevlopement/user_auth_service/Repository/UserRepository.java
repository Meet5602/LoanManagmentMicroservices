package com.CharlesRiverDevlopement.user_auth_service.Repository;

import com.CharlesRiverDevlopement.user_auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
