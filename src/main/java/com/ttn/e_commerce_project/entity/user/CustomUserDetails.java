package com.ttn.e_commerce_project.entity.user;

import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class CustomUserDetails implements UserDetails{

    String email;
    String password;
    boolean isActive;
    boolean isExpired;
    boolean isLocked;
    ZonedDateTime passwordUpdateDate;
    Collection<? extends GrantedAuthority> role;

    public CustomUserDetails(User user)
    {
        this.email=user.getEmail();
        this.isActive = user.isActive();
        this.isLocked = user.isLocked();
        this.isExpired=user.isExpired();
        this.password=user.getPassword();
        this.passwordUpdateDate = user.getPasswordUpdateDate();
        this.role=user.getRole().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getAuthority().name()))
                .toList();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role;
    }

    @Override
    public String getPassword() {
        return this.password ;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return (!this.isExpired);
    }

    @Override
    public boolean isAccountNonLocked() {
        return (!this.isLocked);
    }

    @Override
    public boolean isCredentialsNonExpired( ) {
        long validityDays = 90;
        ZonedDateTime expiryDate = this.passwordUpdateDate.plusDays(validityDays);
        return (ZonedDateTime.now().isBefore(expiryDate));
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

}
