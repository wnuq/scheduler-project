package com.sokol.scheduler.repository;

import com.sokol.scheduler.domain.Role;
import com.sokol.scheduler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClerkId(String clerkId);
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);
}
