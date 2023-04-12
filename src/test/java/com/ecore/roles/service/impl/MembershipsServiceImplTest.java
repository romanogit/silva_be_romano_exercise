package com.ecore.roles.service.impl;

import static com.ecore.roles.utils.FixtureFactory.getDefaultMembership;
import static com.ecore.roles.utils.FixtureFactory.getDeveloperRole;
import static com.ecore.roles.utils.FixtureFactory.getGianniUser;
import static com.ecore.roles.utils.FixtureFactory.getOrdinaryCoralLynxTeam;
import static com.ecore.roles.utils.FixtureFactory.getProductOwnerMembership;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.service.UsersService;

@ExtendWith(MockitoExtension.class)
class MembershipsServiceImplTest {

    @InjectMocks
    private MembershipsServiceImpl membershipsService;
    @Mock
    private MembershipRepository membershipRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UsersService usersService;
    @Mock
    private TeamsService teamsService;

    @Test
    public void shouldCreateMembership() {
        Membership expectedMembership = getDefaultMembership();
        when(roleRepository.findById(expectedMembership.getRole().getId()))
                .thenReturn(Optional.ofNullable(getDeveloperRole()));
        when(membershipRepository.findByRoleIdUserIdAndTeamId(expectedMembership.getRole().getId(),
                expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.empty());
        when(usersService.getUser(getGianniUser().getId()))
                .thenReturn(getGianniUser());
        when(teamsService.getTeam(getOrdinaryCoralLynxTeam().getId()))
                .thenReturn(getOrdinaryCoralLynxTeam());
        when(membershipRepository
                .save(expectedMembership))
                        .thenReturn(expectedMembership);

        Membership actualMembership = membershipsService.assignRoleToMembership(expectedMembership);

        assertEquals(expectedMembership, actualMembership, "Membership is not as expected");
        verify(roleRepository).findById(expectedMembership.getRole().getId());
    }

    @Test
    public void shouldFailToCreateMembershipWhenMembershipsIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.assignRoleToMembership(null), "Unexpected exception");
    }

    @Test
    public void shouldFailToCreateMembershipWhenItExists() {
        Membership expectedMembership = getDefaultMembership();
        when(membershipRepository.findByRoleIdUserIdAndTeamId(expectedMembership.getRole().getId(),
                expectedMembership.getUserId(),
                expectedMembership.getTeamId()))
                        .thenReturn(Optional.of(expectedMembership));

        ResourceExistsException exception = assertThrows(ResourceExistsException.class,
                () -> membershipsService.assignRoleToMembership(expectedMembership));

        assertEquals("Membership already exists", exception.getMessage(), "Incorrect exception message");
        verify(roleRepository, times(0)).getById(any());
        verify(usersService, times(0)).getUser(any());
        verify(teamsService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToCreateMembershipWhenItHasInvalidRole() {
        Membership expectedMembership = getDefaultMembership();
        expectedMembership.setRole(null);

        InvalidArgumentException exception = assertThrows(InvalidArgumentException.class,
                () -> membershipsService.assignRoleToMembership(expectedMembership));

        assertEquals("Invalid 'Role' object", exception.getMessage(), "Incorrect exception message");
        verify(membershipRepository, times(0)).findByUserIdAndTeamId(any(), any());
        verify(roleRepository, times(0)).getById(any());
        verify(usersService, times(0)).getUser(any());
        verify(teamsService, times(0)).getTeam(any());
    }

    @Test
    public void shouldFailToGetMembershipsWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> membershipsService.getMemberships(null));
    }

    @Test
    public void shouldGetNoMembershipsForGivenRoleId() {

        when(membershipRepository.findByRoleId(getDeveloperRole().getId()))
                .thenReturn(List.of());

        List<Membership> memberships = membershipsService.getMemberships(getDeveloperRole().getId());
        assertEquals(0, memberships.size(), "Should be an empty list of membership");
    }

    @Test
    public void shouldGetMembershipsForGivenRoleId() {
        Membership expectedMembership_1 = getProductOwnerMembership();
        Membership expectedMembership_2 = getDefaultMembership();

        when(membershipRepository.findByRoleId(getDeveloperRole().getId()))
                .thenReturn(List.of(expectedMembership_1, expectedMembership_2));

        List<Membership> memberships = membershipsService.getMemberships(getDeveloperRole().getId());
        assertEquals(2, memberships.size(), "Should return only 2 elements");
        assertEquals(expectedMembership_1, memberships.get(0), "Membership 1 is not as expected");
        assertEquals(expectedMembership_2, memberships.get(1), "Membership 2 is not as expected");
    }
}
