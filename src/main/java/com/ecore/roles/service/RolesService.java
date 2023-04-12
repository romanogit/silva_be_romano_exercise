package com.ecore.roles.service;

import java.util.List;
import java.util.UUID;
import com.ecore.roles.model.Role;

public interface RolesService {

    Role createRole(Role role);

    Role getRole(UUID roleId);

    List<Role> getRoles();

    List<Role> getRoles(UUID userId, UUID teamId);
}
