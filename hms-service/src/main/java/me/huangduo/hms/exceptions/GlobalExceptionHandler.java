package me.huangduo.hms.exceptions;

import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.HmsRequest;
import me.huangduo.hms.HmsResponse;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
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
            log.error("The request parameter auto verification failed and get a mapped error", ex);
            return ResponseEntity.badRequest().body(HmsResponse.error(errorCodeEnum.getCode(), errorCodeEnum.getMessage(), errors));
        } catch (Exception e) {
            log.error("The request parameter auto verification failed and get a fallback error", ex);
            return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage(), errors));
        }
    }


    /*
     * Service 层参数校验
     * */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HmsResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("The request parameter verification failed and get a fallback error", ex);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage()));
    }

    /*
     * 认证失败 (业务异常)
     * */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HmsResponse<Void>> handleRecordNotFound(AuthenticationException ex) {
        log.error("The user fails to be authenticated", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HmsResponse.error(ex.getHmsErrorCodeEnum().getCode(), ex.getHmsErrorCodeEnum().getMessage()));
    }

    /*
     * 资源找不到
     * */
    @ExceptionHandler({NoHandlerFoundException.class, RecordNotFoundException.class})
    public ResponseEntity<HmsResponse<Void>> handleNotFoundException(Exception e) {
        log.error("The requested resource could not be found", e);
        if (e instanceof RecordNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HmsResponse.error(((RecordNotFoundException) e).getHmsErrorCodeEnum().getCode(), ((RecordNotFoundException) e).getHmsErrorCodeEnum().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_004.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_004.getMessage()));
    }

    /*
     * 业务异常兜底
     * */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<HmsResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("A business error occurred", e);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_005.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_005.getMessage()));
    }

    /*
     * fallback handler
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HmsResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unknown exception occurs", ex);
        return ResponseEntity.internalServerError().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_001.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_001.getMessage()));
    }
}
