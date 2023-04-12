package com.ecore.roles.exception;

public class InvalidMembershipException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidMembershipException() {
        super("Invalid 'Membership' object. The provided user doesn't belong to the provided team.");
    }
}
