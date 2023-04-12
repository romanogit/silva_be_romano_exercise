package com.ecore.roles.service;

import java.util.List;
import java.util.UUID;
import com.ecore.roles.model.Membership;

public interface MembershipsService {

    Membership assignRoleToMembership(Membership membership);

    List<Membership> getMemberships(UUID roleId);
}
