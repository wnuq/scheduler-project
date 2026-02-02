package com.sokol.scheduler.security;

import com.sokol.scheduler.domain.User;
import com.sokol.scheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String clerkId = oAuth2User.getAttribute("sub"); // Clerk uses 'sub' as the user ID
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        // Sync user to local DB
        User appUser = userService.syncUser(clerkId, email, firstName, lastName);

        // Map local Role to Spring Security Authority
        Set<GrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name())
        );

        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "email" // Attribute to display as principal name
        );
    }
}
