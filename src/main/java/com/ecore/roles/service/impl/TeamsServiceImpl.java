package com.ecore.roles.service.impl;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.service.TeamsService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TeamsServiceImpl implements TeamsService {

    private final TeamsClient teamsClient;

    @Override
    public Team getTeam(@NotNull UUID teamId) {
        ResponseEntity<Team> response = teamsClient.getTeam(teamId);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpClientErrorException(response.getStatusCode(), "Error fetching team");
        }
        return response.getBody();
    }

    @Override
    public List<Team> getTeams() {
        ResponseEntity<List<Team>> response = teamsClient.getTeams();
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpClientErrorException(response.getStatusCode(), "Error fetching teams");
        }
        return response.getBody();
    }
}
