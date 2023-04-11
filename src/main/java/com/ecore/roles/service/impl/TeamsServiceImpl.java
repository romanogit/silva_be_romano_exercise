package com.ecore.roles.service.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.service.TeamsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeamsServiceImpl implements TeamsService {

    private final TeamsClient teamsClient;

    @Override
    public Team getTeam(UUID id) {
        return teamsClient.getTeam(id).getBody();
    }

    @Override
    public List<Team> getTeams() {
        return teamsClient.getTeams().getBody();
    }
}
