package com.grctool.exception.userException;


public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String string) {
        super("Not authorized" + string);
    }
}
