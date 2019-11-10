package pl.longhorn.rebETL.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalTaskException extends ResponseStatusException {

    public IllegalTaskException() {
        super(HttpStatus.BAD_REQUEST, "Bad task");
    }
}
