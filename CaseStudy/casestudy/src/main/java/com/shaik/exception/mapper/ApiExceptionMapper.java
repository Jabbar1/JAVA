package com.shaik.exception.mapper;

import com.shaik.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Created by jabbars on 2/1/2017.
 */
@ControllerAdvice
public class ApiExceptionMapper {

    // Catch All Exception
    @ExceptionHandler({
            Exception.class
    })
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Fault internalError(Exception exception) {
        String code = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        String refId = UUID.randomUUID().toString();
        return new Fault(refId, exception.getMessage(), code);
    }
    @ExceptionHandler({
            BaseException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Fault clientException(BaseException exception) {
        String code = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String refId = UUID.randomUUID().toString();
        return new Fault(refId, exception.getLocalizedMessage(), code);
    }
}
