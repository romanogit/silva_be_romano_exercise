package com.ecore.roles.client;

import java.util.List;
import java.util.UUID;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.ecore.roles.client.model.User;
import com.ecore.roles.configuration.ClientsConfigurationProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UsersClient {

    private final RestTemplate restTemplate;

    private final ClientsConfigurationProperties clientsConfigurationProperties;

    public ResponseEntity<User> getUser(UUID id) {
        return restTemplate.exchange(
                clientsConfigurationProperties.getUsersApiHost() + "/" + id,
                HttpMethod.GET,
                null,
                User.class);
    }

    public ResponseEntity<List<User>> getUsers() {
        return restTemplate.exchange(
                clientsConfigurationProperties.getUsersApiHost(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }
}
