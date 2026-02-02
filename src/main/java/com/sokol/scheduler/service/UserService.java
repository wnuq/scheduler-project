package com.sokol.scheduler.service;

import com.sokol.scheduler.domain.Role;
import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User syncUser(String clerkId, String email, String firstName, String lastName) {
        return userRepository.findByClerkId(clerkId)
                .map(existingUser -> {
                    // Update details if necessary
                    boolean updated = false;
                    if (email != null && !email.equals(existingUser.getEmail())) {
                        existingUser.setEmail(email);
                        updated = true;
                    }
                    if (firstName != null && !firstName.equals(existingUser.getFirstName())) {
                        existingUser.setFirstName(firstName);
                        updated = true;
                    }
                    if (lastName != null && !lastName.equals(existingUser.getLastName())) {
                        existingUser.setLastName(lastName);
                        updated = true;
                    }
                    if (updated) {
                        log.info("Updating user details for clerkId: {}", clerkId);
                        return userRepository.save(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    log.info("Creating new user for clerkId: {}", clerkId);
                    User newUser = new User();
                    newUser.setClerkId(clerkId);
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setRole(Role.STUDENT); // Default role
                    newUser.setRemainingHours(30); // Default hours
                    return userRepository.save(newUser);
                });
    }
}
