package com.ecore.roles.service.impl;

import static java.util.Optional.ofNullable;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.client.model.User;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.InvalidMembershipException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.TeamsService;
import com.ecore.roles.service.UsersService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MembershipsServiceImpl implements MembershipsService {

    private final MembershipRepository membershipRepository;

    private final RoleRepository roleRepository;

    private final UsersService usersService;

    private final TeamsService teamsService;

    @Override
    public Membership assignRoleToMembership(@NonNull Membership membership) {

        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        UUID userId = membership.getUserId();
        UUID teamId = membership.getTeamId();

        if (membershipRepository
                .findByRoleIdUserIdAndTeamId(roleId, userId, teamId)
                .isPresent()) {
            throw new ResourceExistsException(Membership.class);
        }

        roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));

        ofNullable(usersService.getUser(userId))
                .orElseThrow(() -> new ResourceNotFoundException(User.class, userId));

        Team team = ofNullable(teamsService.getTeam(teamId))
                .orElseThrow(() -> new ResourceNotFoundException(Team.class, teamId));

        if (team.getTeamLeadId() != userId) {
            if (!team.getTeamMemberIds().contains(userId)) {
                throw new InvalidMembershipException();
            }
        }

        return membershipRepository.save(membership);
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID roleId) {
        return membershipRepository.findByRoleId(roleId);
    }
}
