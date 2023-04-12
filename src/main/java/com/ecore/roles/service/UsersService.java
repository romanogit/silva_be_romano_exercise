package com.ecore.roles.service;

import com.ecore.roles.client.model.User;

import java.util.List;
import java.util.UUID;

public interface UsersService {

    User getUser(UUID userId);

    List<User> getUsers();
}
