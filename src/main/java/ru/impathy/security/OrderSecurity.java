package ru.impathy.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import ru.impathy.persistence.repository.CustomCarOrderJpaRepository;
import ru.impathy.persistence.repository.InStockCarOrderJpaRepository;

import java.util.UUID;

@Component("orderSecurity")
public class OrderSecurity {
    private final InStockCarOrderJpaRepository inStockRepository;
    private final CustomCarOrderJpaRepository customRepository;

    public OrderSecurity(InStockCarOrderJpaRepository inStockRepository,
                         CustomCarOrderJpaRepository customRepository) {
        this.inStockRepository = inStockRepository;
        this.customRepository = customRepository;
    }

    public boolean isInStockOwner(UUID orderId, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        if (userId == null) {
            return false;
        }
        return inStockRepository.findByIdAndClient_IdAndRemovedFalse(orderId, userId).isPresent();
    }

    public boolean isCustomOwner(UUID orderId, Authentication authentication) {
        UUID userId = extractUserId(authentication);
        if (userId == null) {
            return false;
        }
        return customRepository.findByIdAndClient_IdAndRemovedFalse(orderId, userId).isPresent();
    }

    private UUID extractUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return null;
        }
        try {
            return UUID.fromString(jwt.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
