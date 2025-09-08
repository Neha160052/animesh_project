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


        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(SYSTEM_AUDITOR);
        }

        Object principal = authentication.getPrincipal();


        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return Optional.ofNullable(userDetails.getUsername()).filter(name -> !name.isBlank());
        }


        if (principal instanceof String principalName) {
            if ("anonymousUser".equalsIgnoreCase(principalName)) {
                return Optional.of(SYSTEM_AUDITOR);
            }
            return Optional.of(principalName);
        }


        return Optional.of(SYSTEM_AUDITOR);
    }
}
