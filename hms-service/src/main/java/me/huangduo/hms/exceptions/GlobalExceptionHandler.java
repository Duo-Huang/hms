package me.huangduo.hms.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.request.HmsRequest;
import me.huangduo.hms.dto.response.HmsResponse;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*
     * 400 - Controller层请求参数校验, MethodArgumentNotValidException 为Spring 校验器抛出, 用于校验body
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HmsResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) throws NoSuchFieldException {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                // Handling field level errors
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                // Handling class level errors
                errors.put("requestBody", error.getDefaultMessage());
            }
        });
        try {
            HmsErrorCodeEnum errorCodeEnum = (HmsErrorCodeEnum) HmsRequest.class.getMethod("getHmsErrorCodeEnum").invoke(ex.getBindingResult().getTarget());
            log.error("The request parameter auto verification failed and get a mapped error.", ex);
            return ResponseEntity.badRequest().body(HmsResponse.error(errorCodeEnum.getCode(), errorCodeEnum.getMessage(), errors));
        } catch (Exception e) {
            log.error("The request parameter auto verification failed and get a fallback error.", ex);
            return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage(), errors));
        }
    }

    /*
     * 400 - Controller层请求参数校验, ConstraintViolationException 为Spring 校验器抛出,用于校验单个参数
     * */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HmsResponse<Map<String, String>>> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            Path propertyPath = violation.getPropertyPath();
            String lastFieldName = null;

            for (Path.Node node : propertyPath) {
                lastFieldName = node.getName();
            }

            errors.put(Objects.requireNonNullElse(lastFieldName, "unknown"), violation.getMessage());
        });
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage(), errors));
    }

    /*
     * 400 - Service 层参数校验
     * */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HmsResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("The request parameter verification failed and get a fallback error.", ex);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_003.getMessage(), ex.getMessage()));
    }

    /*
    * 400
    * */
    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<HmsResponse<Void>> handleBadRequestException(Exception e) {
        log.error("Bad request", e);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_008.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_008.getMessage()));
    }

    /*
     * 401 - 认证失败 (业务异常)
     * */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HmsResponse<Void>> handleRecordNotFound(AuthenticationException ex) {
        log.error("The user fails to be authenticated.", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HmsResponse.error(ex.getHmsErrorCodeEnum().getCode(), ex.getHmsErrorCodeEnum().getMessage()));
    }

    /*
     * 403 - 鉴权失败 (业务异常)
     * */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HmsResponse<Void>> handleRecordNotFound(AccessDeniedException ex) {
        log.error("The user fails to be authorized.", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(HmsResponse.error(ex.getHmsErrorCodeEnum().getCode(), ex.getHmsErrorCodeEnum().getMessage()));
    }

    /*
     * 404
     * */
    @ExceptionHandler({NoHandlerFoundException.class, RecordNotFoundException.class})
    public ResponseEntity<HmsResponse<Void>> handleNotFoundException(Exception e) {
        log.error("The requested resource could not be found.", e);
        if (e instanceof RecordNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HmsResponse.error(((RecordNotFoundException) e).getHmsErrorCodeEnum().getCode(), ((RecordNotFoundException) e).getHmsErrorCodeEnum().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_004.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_004.getMessage()));
    }

    /*
     * 405
     * */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HmsResponse<Void>> handleMethodNotAllowException(HttpRequestMethodNotSupportedException e) {
        log.error("Request method is not supported", e);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_006.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_006.getMessage()));
    }

    /*
     * 415
     * */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<HmsResponse<Void>> handleMethodNotAllowException(HttpMediaTypeNotSupportedException e) {
        log.error("Request media type is not supported", e);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_007.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_007.getMessage()));
    }

    /*
     * 业务异常兜底
     * */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<HmsResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("A business error occurred.", e);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_005.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_005.getMessage()));
    }

    /*
     * 500 - fallback handler
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HmsResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unknown exception occurs.", ex);
        return ResponseEntity.internalServerError().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_001.getCode(), HmsErrorCodeEnum.SYSTEM_ERROR_001.getMessage()));
    }
}
