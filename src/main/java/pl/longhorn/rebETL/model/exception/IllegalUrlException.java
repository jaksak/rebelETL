package pl.longhorn.rebETL.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalUrlException extends ResponseStatusException {


    public IllegalUrlException() {
        super(HttpStatus.BAD_REQUEST, "invalid-url");
    }
}
