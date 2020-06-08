package dania.app.web.errorHandler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = setApiErrorValues(HttpStatus.BAD_REQUEST, ex);

        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(NumberFormatException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            NumberFormatException ex) {
        ApiError apiError = setApiErrorValues(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(
            BadRequestException ex) {
        ApiError apiError = setApiErrorValues(HttpStatus.BAD_REQUEST, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleMySQLIntegrityConstraintViolationFound(
            DataIntegrityViolationException ex) {
        ApiError apiError = setApiErrorValues(HttpStatus.BAD_REQUEST, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleMySQLIntegrityConstraintViolationFound(
            ConstraintViolationException ex) {
        ApiError apiError = setApiErrorValues(HttpStatus.BAD_REQUEST, ex);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    protected ResponseEntity<Object> handleUnsupportedOperationExceptionFound(
            UnsupportedOperationException ex) {
        ApiError apiError = setApiErrorValues(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(
            Exception ex) {
        ApiError apiError = setApiErrorValues(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ApiError setApiErrorValues(HttpStatus hhtpStatus, Exception ex) {
        ApiError apiError = new ApiError(hhtpStatus);
        String message = ex.getMessage();

        if (message == null && ex.getCause() != null) {
            message = ex.getCause().getMessage();

            if (message == null && ex.getCause().getCause() != null) {
                message = ex.getCause().getCause().getMessage();
            }
        }
        apiError.setMessage(message);
        apiError.setDebugMessage(ex.getLocalizedMessage());
        ex.printStackTrace();

        return apiError;
    }
}

