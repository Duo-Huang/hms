package me.huangduo.hms.exceptions;

import me.huangduo.hms.HmsRequest;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HmsResponse<?>> handleRecordNotFound(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HmsResponse.error(HmsErrorCodeEnum.USER_ERROR_101.getCode(), HmsErrorCodeEnum.USER_ERROR_101.getMessage()));

    }

    /*
     * Controller层请求参数校验, MethodArgumentNotValidException 为Spring 校验器抛出
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HmsResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) throws NoSuchFieldException {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        try {
            HmsErrorCodeEnum errorCodeEnum = (HmsErrorCodeEnum) HmsRequest.class.getMethod("getHmsErrorCodeEnum").invoke(ex.getBindingResult().getTarget());
            return ResponseEntity.badRequest().body(HmsResponse.error(errorCodeEnum.getCode(), errorCodeEnum.getMessage(), errors));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage()));
        }
    }


    /*
     * Service 层参数校验
     * */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HmsResponse<?>> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage()));
    }

    /*
     * fallback handler
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HmsResponse<?>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_001.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_001.getMessage()));
    }
}
