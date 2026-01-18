package com.task_service.task_service.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class JwtContext {

    @SuppressWarnings("unchecked")
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUserId() {
        return (Long) getAuthentication().getPrincipal();
    }

    public String getUserType() {
        return (String) getAuthentication().getDetails();
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermissions() {
        return (List<String>) getAuthentication().getCredentials();
    }

    public boolean isSuperAdmin() {
        return "SUPER_ADMIN".equals(getUserType());
    }

    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }
}
