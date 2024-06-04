package com.bol.mancala.exception.handler;

import com.bol.mancala.exception.IllegalMoveException;
import com.bol.mancala.exception.InvalidRequestException;
import com.bol.mancala.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.MissingRequestValueException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalMoveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleIllegalMoveException(IllegalMoveException ex) {
        return Mono.just(getProblemDetail(HttpStatus.BAD_REQUEST, "Requested move is illegal", ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return Mono.just(getProblemDetail(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Mono<ProblemDetail> handleInvalidRequestException(InvalidRequestException ex) {
        return Mono.just(getProblemDetail(HttpStatus.NOT_ACCEPTABLE, "Invalid request", ex.getMessage()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return Mono.just(ex.getBody());
    }

    @ExceptionHandler(MissingRequestValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ProblemDetail> handleHandlerMissingRequestValueException(MissingRequestValueException ex) {
        return Mono.just(ex.getBody());
    }

    private static ProblemDetail getProblemDetail(HttpStatusCode status, String title, String message) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(message);
        return problemDetail;
    }

}
