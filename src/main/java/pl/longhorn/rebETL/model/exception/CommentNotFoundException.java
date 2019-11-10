package pl.longhorn.rebETL.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CommentNotFoundException extends ResponseStatusException {

    public CommentNotFoundException() {
        super(HttpStatus.BAD_REQUEST, "comment-not-found");
    }
}
