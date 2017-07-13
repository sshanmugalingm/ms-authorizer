package au.com.bglcorp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by senthurshanmugalingm on 13/07/2017.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnAuthorizedAccessException extends RuntimeException {

    public UnAuthorizedAccessException(String message) {
        super(message);
    }
}
