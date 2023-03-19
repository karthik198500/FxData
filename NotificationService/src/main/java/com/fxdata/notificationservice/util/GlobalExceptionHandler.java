package com.fxdata.notificationservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/*Enable this class only for production config when you do not want to show the body of the exception*/
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("Error caught in the Global Exception Handler. ",ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }
        return new ResponseEntity<Object>(new ApiError(status,ex), status);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object>  defaultErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let the framework handle it example
        // at the start of this post. AnnotationUtils is a Spring Framework utility class.
        // Refer to https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
        logger.error("Error caught in the Global Exception Handler. ",ex);
        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null){
            throw ex;
        }
        if(ex instanceof ResponseStatusException){
            HttpStatus status = ((ResponseStatusException) ex).getStatus();
            return new ResponseEntity<Object>(new ApiError(status,ex), status);
        }
        return new ResponseEntity<Object>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
