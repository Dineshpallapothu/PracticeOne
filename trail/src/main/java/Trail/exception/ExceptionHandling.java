package Trail.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(Exception e){
        ProblemDetail problemDetail = null;
        if (e instanceof BadCredentialsException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(401), e.getMessage());
            problemDetail.setProperty("access_reason", "Authentication Failure");
        }
        if(e instanceof AccessDeniedException){
            problemDetail=ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403),e.getMessage());
            problemDetail.setProperty("access denied","not Authorized");
        }
        if (e instanceof SignatureException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), e.getMessage());
            problemDetail.setProperty("denied_reason", "JWT signature no valid");
        }
        if (e instanceof ExpiredJwtException) {
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), e.getMessage());
            problemDetail.setProperty("denied_reason", "Token Expired");
        }
        return problemDetail;
    }
}
