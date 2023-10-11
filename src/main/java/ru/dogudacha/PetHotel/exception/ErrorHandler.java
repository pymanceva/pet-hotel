package ru.dogudacha.PetHotel.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> errorMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.error("EH: MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> errorMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.error("EH: MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage() + ". Param:" + ex.getName() + " Value=" + ex.getValue())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> errorMissingServletRequestParameterException(final MissingServletRequestParameterException ex) {
        log.error("EH: MissingServletRequestParameterException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Incorrectly made request")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> errorDataIntegrityViolationException(final DataIntegrityViolationException ex) {
        log.error("EH: DataIntegrityViolationException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(Objects.requireNonNull(ex.getRootCause()).getMessage())
                .reason("Integrity constraint has been violated.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> errorConstraintViolationException(final ConstraintViolationException ex) {
        log.error("EH: ConstraintViolationException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getCause().getMessage())
                .reason("Integrity constraint has been violated.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException ex) {
        log.error("EH: NotFoundException: {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("The required object was not found.")
                .httpStatus(HttpStatus.NOT_FOUND)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(NotChangeableException.class)
    public ResponseEntity<ApiError> handleNotChangeableException(final NotChangeableException ex) {
        log.error("EH: NotChangeableException: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Incorrectly made request.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(InitiatorException.class)
    public ResponseEntity<ApiError> handleInitiatorException(final InitiatorException ex) {
        log.error("EH: InitiatorException: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Initiator can not be requester.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(LimitException.class)
    public ResponseEntity<ApiError> handleLimitException(final LimitException ex) {
        log.error("EH: LimitException: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(NotPublishedException.class)
    public ResponseEntity<ApiError> handleNotPublishedException(final NotPublishedException ex) {
        log.error("EH: NotPublishedException: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Event must be published.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiError> handleInvalidDateRangeException(final InvalidDateRangeException ex) {
        log.error("EH: InvalidDateRangeException: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("For the requested operation the conditions are not met.")
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ApiError> handleHttpMessageConversionException(final HttpMessageConversionException ex) {
        log.error("EH: HttpMessageConversionException: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .message(ex.getMessage())
                .reason("Event must be published.")
                .httpStatus(HttpStatus.CONFLICT)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiError> errorThrowableException(final Throwable ex) {
        log.error("EH: Internal Server Error. {}", ex.getMessage(), ex);
        ApiError apiError = ApiError.builder()
                .errors(Arrays.stream(ex.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList()))
                .message(ex.getMessage())
                .reason("Internal Server Error.")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .timeStamp(now())
                .build();
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }
}
