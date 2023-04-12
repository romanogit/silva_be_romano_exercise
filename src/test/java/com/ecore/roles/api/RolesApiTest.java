package com.ecore.roles.api;

import static com.ecore.roles.utils.FixtureFactory.GIANNI_USER_UUID;
import static com.ecore.roles.utils.FixtureFactory.ORDINARY_CORAL_LYNX_TEAM_UUID;
import static com.ecore.roles.utils.FixtureFactory.UUID_1;
import static com.ecore.roles.utils.FixtureFactory.getDefaultMembership;
import static com.ecore.roles.utils.FixtureFactory.getDevOpsRole;
import static com.ecore.roles.utils.FixtureFactory.getDeveloperRole;
import static com.ecore.roles.utils.FixtureFactory.getGianniUser;
import static com.ecore.roles.utils.FixtureFactory.getOrdinaryCoralLynxTeam;
import static com.ecore.roles.utils.FixtureFactory.getProductOwnerRole;
import static com.ecore.roles.utils.FixtureFactory.getTesterRole;
import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.MockUtils.mockGetUserById;
import static com.ecore.roles.utils.RestAssuredHelper.createMembership;
import static com.ecore.roles.utils.RestAssuredHelper.createRole;
import static com.ecore.roles.utils.RestAssuredHelper.getRole;
import static com.ecore.roles.utils.RestAssuredHelper.getRoles;
import static com.ecore.roles.utils.RestAssuredHelper.sendRequest;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.RoleDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RolesApiTest {

    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public RolesApiTest(RestTemplate restTemplate, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
        Optional<Role> devOpsRole = roleRepository.findByName(getDevOpsRole().getName());
        devOpsRole.ifPresent(roleRepository::delete);
    }

    @Test
    void shouldFailWhenPathDoesNotExist() {
        sendRequest(when()
                .get("/v1/role")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldCreateNewRole() {
        Role expectedRole = getDevOpsRole();

        RoleDto actualRole = createRole(expectedRole)
                .statusCode(200)
                .extract().as(RoleDto.class);

        assertThat(actualRole.getName()).isEqualTo(expectedRole.getName());
    }

    @Test
    void shouldFailToCreateNewRoleWhenNull() {
        createRole(null)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenMissingName() {
        createRole(Role.builder().build())
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenBlankName() {
        createRole(Role.builder().name("").build())
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenNameAlreadyExists() {
        createRole(getDeveloperRole())
                .validate(400, "Role already exists");
    }

    @Test
    void shouldGetAllRoles() {
        RoleDto[] roles = getRoles()
                .extract().as(RoleDto[].class);

        assertThat(roles.length).isGreaterThanOrEqualTo(3);
        assertThat(roles).contains(RoleDto.fromModel(getDeveloperRole()));
        assertThat(roles).contains(RoleDto.fromModel(getProductOwnerRole()));
        assertThat(roles).contains(RoleDto.fromModel(getTesterRole()));
    }

    @Test
    void shouldGetRoleById() {
        Role expectedRole = getDeveloperRole();

        getRole(expectedRole.getId())
                .statusCode(200)
                .body("name", equalTo(expectedRole.getName()));
    }

    @Test
    void shouldFailToGetRoleById() {
        getRole(UUID_1)
                .validate(404, format("Role %s not found", UUID_1));
    }

    @Test
    void shouldGetRoleByUserIdAndTeamId() {
        Membership expectedMembership = getDefaultMembership();
        mockGetUserById(mockServer, GIANNI_USER_UUID, getGianniUser());
        mockGetTeamById(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, getOrdinaryCoralLynxTeam());
        createMembership(expectedMembership)
                .statusCode(200);

        getRole(expectedMembership.getUserId(), expectedMembership.getTeamId())
                .statusCode(200)
                .body("name[0]", equalTo(expectedMembership.getRole().getName()));
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenMissingUserId() {
        getRole(null, ORDINARY_CORAL_LYNX_TEAM_UUID)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenMissingTeamId() {
        getRole(GIANNI_USER_UUID, null)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldReturnEmptyWhenUserIdAndTeamIdDoesNotExist() {
        Membership expectedMembership = getDefaultMembership();
        RoleDto[] result = getRole(expectedMembership.getUserId(), expectedMembership.getTeamId())
                .extract().as(RoleDto[].class);

        assertEquals(0, result.length, "Result should be empty");
    }
}
