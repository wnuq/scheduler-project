package com.sokol.scheduler.web;

import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    @ModelAttribute("user")
    public User addUserToModel(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return null;
        
        String clerkId = principal.getAttribute("sub");
        return userRepository.findByClerkId(clerkId).orElse(null);
    }

    @ModelAttribute("remainingHours")
    public Integer addRemainingHoursToModel(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return null;

        String clerkId = principal.getAttribute("sub");
        return userRepository.findByClerkId(clerkId)
                .map(User::getRemainingHours)
                .orElse(null);
    }
}
