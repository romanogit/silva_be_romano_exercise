package com.ecore.roles.service.impl;

import static com.ecore.roles.utils.FixtureFactory.UUID_1;
import static com.ecore.roles.utils.FixtureFactory.getDeveloperRole;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;

@ExtendWith(MockitoExtension.class)
class RolesServiceImplTest {

    @InjectMocks
    private RolesServiceImpl rolesService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipsService membershipsService;

    @Test
    public void shouldCreateRole() {
        Role developerRole = getDeveloperRole();
        when(roleRepository.save(developerRole)).thenReturn(developerRole);

        Role role = rolesService.createRole(developerRole);

        assertEquals(developerRole, role, "Role is not as expected");
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> rolesService.createRole(null), "Incorrect exception thrown");
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleAlreadyExists() {
        Role developerRole = getDeveloperRole();
        when(roleRepository.findByName(developerRole.getName())).thenReturn(Optional.of(developerRole));

        assertThrows(ResourceExistsException.class,
                () -> rolesService.createRole(developerRole), "Incorrect exception thrown");
    }

    @Test
    public void shouldReturnNoRolesWhenNoneExists() {
        when(roleRepository.findAll()).thenReturn(List.of());

        List<Role> roles = rolesService.getRoles();

        assertEquals(0, roles.size(), "Expected no roles to be returned");
    }

    @Test
    public void shouldReturnAllRoles() {
        Role developerRole = getDeveloperRole();
        when(roleRepository.findAll()).thenReturn(List.of(developerRole));

        List<Role> roles = rolesService.getRoles();
        assertEquals(1, roles.size(), "Should return only 1 element");
        assertEquals(developerRole, roles.get(0), "Role 1 is not as expected");
    }

    @Test
    public void shouldGetRoleWhenTeamIdExists() {
        Role developerRole = getDeveloperRole();
        when(roleRepository.findById(developerRole.getId())).thenReturn(Optional.of(developerRole));

        Role role = rolesService.getRole(developerRole.getId());

        assertEquals(developerRole, role, "Role is not as expected");
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRole(UUID_1));

        assertEquals(format("Role %s not found", UUID_1), exception.getMessage(),
                "Incorrect exception message");
    }
}
