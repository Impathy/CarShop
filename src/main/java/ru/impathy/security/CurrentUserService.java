package ru.impathy.security;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.impathy.domain.exeptions.DomainValidationException;

import java.util.Set;
import java.util.UUID;

@Component
public class CurrentUserService {
    private final UUID testFallbackUserId;
    private final Environment environment;

    public CurrentUserService(Environment environment) {
        this.environment = environment;
        this.testFallbackUserId = UUID.fromString("00000000-0000-0000-0000-000000000004");
    }

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            try {
                return UUID.fromString(jwt.getSubject());
            } catch (Exception e) {
                throw new DomainValidationException("JWT subject must be UUID");
            }
        }
        Set<String> activeProfiles = Set.of(environment.getActiveProfiles());
        if (activeProfiles.contains("test")) {
            return testFallbackUserId;
        }
        throw new DomainValidationException("Authenticated JWT principal is required");
    }
}
