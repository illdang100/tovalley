package kr.ac.kumoh.illdang100.tovalley.handler;

import kr.ac.kumoh.illdang100.tovalley.dto.ResponseDto;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomApiException;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.CustomValidationException;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.OpenApiException;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.SuspensionException;
import kr.ac.kumoh.illdang100.tovalley.handler.ex.UnauthorizedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> reIssueTokenException(UnauthorizedAccessException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SuspensionException.class)
    public ResponseEntity<?> suspensionException(SuspensionException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationApiException(CustomValidationException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> typeMismatchApiException(HttpMessageNotReadableException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, "typeMismatch", null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OpenApiException.class)
    public ResponseEntity<?> openApiFetchException(OpenApiException e) {

        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}