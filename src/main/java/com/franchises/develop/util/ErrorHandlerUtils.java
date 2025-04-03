package com.franchises.develop.util;

import com.franchises.develop.dto.response.ResponseDTO;
import com.franchises.develop.exception.handler.*;
import com.mongodb.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ErrorHandlerUtils {

    private ErrorHandlerUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final Map<Class<? extends Throwable>, HttpStatus> ERROR_MAPPINGS = Map.of(
            DuplicateKeyException.class, HttpStatus.CONFLICT,
            ResourceNotFoundException.class, HttpStatus.NOT_FOUND,
            BranchAlreadyExistsException.class, HttpStatus.CONFLICT,
            BranchNotFoundException.class, HttpStatus.NOT_FOUND,
            BranchNameAlreadyUpToDateException.class, HttpStatus.BAD_REQUEST,
            ProductNotFoundException.class, HttpStatus.NOT_FOUND,
            ProductAlreadyExistsException.class, HttpStatus.CONFLICT,
            ProductNameAlreadyUpToDateException.class, HttpStatus.BAD_REQUEST
    );

    /**
     * Handles an error and constructs a standardized error response.
     *
     * @param error The exception or error that occurred.
     * @param <T>   The type of the expected response data.
     * @return A {@link Mono} emitting a {@link ResponseDTO} containing error details.
     */
    public static <T> Mono<ResponseDTO<T>> handleError(Throwable error) {
        HttpStatus status = ERROR_MAPPINGS.getOrDefault(
                error.getClass(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );

        String message = (error instanceof Exception)
                ? error.getMessage()
                : Constants.MESSAGE_ERROR;

        return Mono.just(buildErrorResponse(status, message));
    }

    /**
     * Builds a standardized error response.
     *
     * @param status  The HTTP status code.
     * @param message The error message.
     * @param <T>     The type of the expected response data.
     * @return A {@link ResponseDTO} containing error details.
     */
    public static <T> ResponseDTO<T> buildErrorResponse(HttpStatus status, String message) {
        return ResponseDTO.<T>builder()
                .code(status.value())
                .message(message)
                .response(null)
                .build();
    }
}

