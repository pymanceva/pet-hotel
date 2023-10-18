package ru.dogudacha.PetHotel.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.InvalidDateRangeException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.exception.dto.ApiError;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    record Error(String message, String reason, HttpStatus httpStatus, LocalDateTime timeStamp) {
    }


    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleMissingRequestHeaderException(final MissingRequestHeaderException ex) {
        log.error("EH: MissingRequestHeaderException: {}", ex.getMessage(), ex);

        return new Error(
                ex.getMessage(),
                "Required request header is not present.",
                HttpStatus.BAD_REQUEST,
                now()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error handleAccessDeniedException(final AccessDeniedException ex) {
        log.error("EH: AccessDeniedException: {}", ex.getMessage(), ex);

        return new Error(
                ex.getMessage(),
                "Operation is denied for this user.",
                HttpStatus.FORBIDDEN,
                now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError errorMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.error("EH: MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError errorMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.error("EH: MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message(ex.getMessage() + ". Param:" + ex.getName() + " Value=" + ex.getValue())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError errorMissingServletRequestParameterException(final MissingServletRequestParameterException ex) {
        log.error("EH: MissingServletRequestParameterException: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError errorDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        log.error("EH: DataIntegrityViolationException: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message(Objects.requireNonNull(ex.getRootCause()).getMessage())
                .reason("Integrity constraint has been violated.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError errorConstraintViolationException(final ConstraintViolationException ex) {
        log.error("EH: ConstraintViolationException: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message(ex.getCause().getMessage())
                .reason("Integrity constraint has been violated.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException ex) {
        log.error("EH: NotFoundException: {}", ex.getMessage(), ex);
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("The required object was not found.")
                .httpStatus(HttpStatus.NOT_FOUND)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidDateRangeException(final InvalidDateRangeException ex) {
        log.error("EH: InvalidDateRangeException: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleHttpMessageConversionException(final HttpMessageConversionException ex) {
        log.error("EH: HttpMessageConversionException: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Request parameters validation error.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError errorThrowableException(final Throwable ex) {
        log.error("EH: Internal Server Error. {}", ex.getMessage(), ex);
        return ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(ex.getMessage())
                .reason("Internal Server Error.")
                .httpStatus(HttpStatus.I_AM_A_TEAPOT)
                .timeStamp(now())
                .build();
    }
}
