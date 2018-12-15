package com.offer.offerservice.util.exception;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.offer.offerservice.model.Error;

@ControllerAdvice
@RestController
public abstract class CustomizedResponseEntityExceptionHandler {

    final Logger log;

    public CustomizedResponseEntityExceptionHandler(final Logger log) {
        this.log = log;
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleInvalidRequestException(final InvalidRequestException e) {
        log.warn(e.getMessage(), e);
        return new Error(e.getMessage());
    }

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleOfferNotFoundException(final OfferNotFoundException e) {
        log.warn(e.getMessage(), e);
        return new Error(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleRuntimeException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return new Error(e.getMessage());
    }

}