package com.retailer.reward.exception;

import com.retailer.reward.util.APIResponse;
import com.retailer.reward.util.ProgramConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidIDException.class, UnsuccessfulOperationException.class})
    public ResponseEntity<APIResponse> handleInputValidationException(Exception exception) {

        APIResponse errorResponse = new APIResponse(ProgramConstants.FAILURE, exception.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}




