package com.ecore.roles.service.impl;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import com.ecore.roles.client.UsersClient;
import com.ecore.roles.client.model.User;
import com.ecore.roles.service.UsersService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsersServiceImpl implements UsersService {

    private final UsersClient usersClient;

    @Override
    public User getUser(@NotNull UUID userId) {
        ResponseEntity<User> response = usersClient.getUser(userId);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpClientErrorException(response.getStatusCode(), "Error fetching user");
        }

        return response.getBody();
    }

    @Override
    public List<User> getUsers() {
        ResponseEntity<List<User>> response = usersClient.getUsers();
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpClientErrorException(response.getStatusCode(), "Error fetching users");
        }
        return response.getBody();
    }
}
