package com.grctool.exception.userException;

import java.util.UUID;

public class UserDoesNotExist extends RuntimeException {
    public UserDoesNotExist(UUID id) {
        super("User does not exist with id: " + id);
    }
}
