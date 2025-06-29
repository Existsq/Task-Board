package com.work.auth.repository;

import com.work.auth.model.AuthUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

  Optional<AuthUser> findByEmail(String email);
}
