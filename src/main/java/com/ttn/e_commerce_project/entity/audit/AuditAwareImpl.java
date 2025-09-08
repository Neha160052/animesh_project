package com.ttn.e_commerce_project.entity.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.ttn.e_commerce_project.constants.UserConstants.SYSTEM_AUDITOR;

@Component("AuditAware")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Case 1: No authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(SYSTEM_AUDITOR);
        }

        Object principal = authentication.getPrincipal();

        // Case 2: Handle Spring Security UserDetails
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return Optional.ofNullable(userDetails.getUsername()).filter(name -> !name.isBlank());
        }

        // Case 3: Principal is a String (e.g., "anonymousUser")
        if (principal instanceof String principalName) {
            if ("anonymousUser".equalsIgnoreCase(principalName)) {
                return Optional.of(SYSTEM_AUDITOR);
            }
            return Optional.of(principalName);
        }

        // Case 4: Unknown principal type
        return Optional.of(SYSTEM_AUDITOR);
    }
}
