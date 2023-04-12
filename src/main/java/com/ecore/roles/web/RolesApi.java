package com.ecore.roles.web;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import com.ecore.roles.web.dto.RoleDto;

public interface RolesApi {

    ResponseEntity<RoleDto> createRole(
            RoleDto roleDto);

    ResponseEntity<List<RoleDto>> getRoles();

    ResponseEntity<List<RoleDto>> getRoles(
            UUID userId,
            UUID teamId);

    ResponseEntity<RoleDto> getRole(
            UUID roleId);
}
