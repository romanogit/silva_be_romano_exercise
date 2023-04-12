package com.ecore.roles.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ecore.roles.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, UUID> {

    @Query("SELECT m from Membership as m where m.role.id = :roleId and m.userId = :userId and m.teamId = :teamId")
    Optional<Membership> findByRoleIdUserIdAndTeamId(UUID roleId, UUID userId, UUID teamId);

    List<Membership> findByUserIdAndTeamId(UUID userId, UUID teamId);

    List<Membership> findByRoleId(UUID roleId);
}
