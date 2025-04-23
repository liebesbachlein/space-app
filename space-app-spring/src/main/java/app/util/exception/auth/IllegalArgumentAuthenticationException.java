package app.util.exception.auth;

import org.springframework.security.core.AuthenticationException;

public class IllegalArgumentAuthenticationException extends AuthenticationException {
    public IllegalArgumentAuthenticationException(String msg) {
        super(msg);
    }
}