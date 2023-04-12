package com.ecore.roles.service.impl;

import static com.ecore.roles.utils.FixtureFactory.UUID_1;
import static com.ecore.roles.utils.FixtureFactory.getGianniUser;
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
import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    @InjectMocks
    private UsersServiceImpl usersService;
    @Mock
    private UsersClient usersClient;

    @Test
    void shouldGetUserWhenUserIdExists() {
        User gianniUser = getGianniUser();
        when(usersClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(gianniUser));

        User user = usersService.getUser(UUID_1);

        assertEquals(gianniUser, user, "Unexpected user returned");
    }

    @Test
    public void shouldGetAnEmptyBodyWhenUserIdDoesNotExist() {
        when(usersClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK).build());

        assertNull(usersService.getUser(UUID_1), "Should return an empty body");
    }

    @Test
    public void shouldFailToGetUserWhenHttpStatusNotOK() {
        when(usersClient.getUser(UUID_1))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE).build());

        assertThrows(HttpClientErrorException.class,
                () -> usersService.getUser(UUID_1), "Unexpected exception");
    }

    @Test
    public void shouldGetEmptyUserList() {
        when(usersClient.getUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK).body(List.of()));

        List<User> users = usersService.getUsers();
        assertEquals(0, users.size(), "Should return an empty list of users");
    }

    @Test
    public void shouldGetAllUsersList() {
        User gianniUser = getGianniUser();
        when(usersClient.getUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK).body(List.of(gianniUser)));

        List<User> users = usersService.getUsers();
        assertEquals(1, users.size(), "Should return only 1 element");
        assertEquals(gianniUser, users.get(0), "Team 1 is not as expected");
    }

    @Test
    public void shouldFailToGetUsersWhenHttpStatusNotOK() {
        when(usersClient.getUsers())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_ACCEPTABLE).build());

        assertThrows(HttpClientErrorException.class,
                () -> usersService.getUsers(), "Unexpected exception");
    }
}
