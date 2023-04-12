package com.ecore.roles.service.impl;

import static com.ecore.roles.utils.FixtureFactory.ORDINARY_CORAL_LYNX_TEAM_UUID;
import static com.ecore.roles.utils.FixtureFactory.getOrdinaryCoralLynxTeam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;

@ExtendWith(MockitoExtension.class)
class TeamsServiceImplTest {

    @InjectMocks
    private TeamsServiceImpl teamsService;

    @Mock
    private TeamsClient teamsClient;

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team ordinaryCoralLynxTeam = getOrdinaryCoralLynxTeam();
        when(teamsClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ordinaryCoralLynxTeam));

        Team team = teamsService.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID);

        assertEquals(ordinaryCoralLynxTeam, team, "Unexpected team returned");
    }

    @Test
    public void shouldGetAnEmptyBodyWhenTeamIdDoesNotExist() {
        when(teamsClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK).build());

        assertNull(teamsService.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID), "Should return an empty body");
    }

    @Test
    public void shouldFailToGetTeamWhenHttpStatusNotOK() {
        when(teamsClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE).build());

        assertThrows(HttpClientErrorException.class,
                () -> teamsService.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID), "Unexpected exception");
    }

    @Test
    public void shouldGetEmptyTeamList() {
        when(teamsClient.getTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK).body(List.of()));

        List<Team> teams = teamsService.getTeams();
        assertEquals(0, teams.size(), "Should return an empty list of teams");
    }

    @Test
    public void shouldGetAllTeamsList() {
        Team ordinaryCoralLynxTeam = getOrdinaryCoralLynxTeam();
        when(teamsClient.getTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK).body(List.of(ordinaryCoralLynxTeam)));

        List<Team> teams = teamsService.getTeams();
        assertEquals(1, teams.size(), "Should return only 1 element");
        assertEquals(ordinaryCoralLynxTeam, teams.get(0), "Team 1 is not as expected");
    }

    @Test
    public void shouldFailToGetTeamsWhenHttpStatusNotOK() {
        when(teamsClient.getTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE).build());

        assertThrows(HttpClientErrorException.class,
                () -> teamsService.getTeams(), "Unexpected exception");
    }
}
