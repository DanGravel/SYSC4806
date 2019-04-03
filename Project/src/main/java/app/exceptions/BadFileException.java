package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/***
 * Exception used to send back a 400 if the file doesn't exist
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad file request")
public class BadFileException extends RuntimeException {
}
