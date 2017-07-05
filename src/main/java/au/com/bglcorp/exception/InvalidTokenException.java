package au.com.bglcorp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by senthurshanmugalingm on 5/07/2017.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

}
