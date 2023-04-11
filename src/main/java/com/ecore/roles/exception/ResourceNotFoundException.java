package com.ecore.roles.exception;

import java.util.UUID;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public <T> ResourceNotFoundException(Class<T> resource, UUID id) {
        super(format("%s %s not found", resource.getSimpleName(), id));
    }
}
