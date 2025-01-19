package me.huangduo.hms.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import me.huangduo.hms.dto.request.HmsRequest;
import me.huangduo.hms.dto.response.HmsResponse;
import me.huangduo.hms.enums.HmsErrorCodeEnum;
import me.huangduo.hms.enums.HmsHttpStatusEnum;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {

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
            return ResponseEntity.badRequest().body(HmsResponse.error(errorCodeEnum, errors));
        } catch (Exception e) {
            log.error("The request parameter auto verification failed and get a fallback error.", ex);
            return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003, errors));
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
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003, errors));
    }

    /*
     * 400 - Service 层参数校验
     * */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HmsResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("The request parameter verification failed and get a fallback error.", ex);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_003, ex.getMessage()));
    }

    /*
     * 401 - 认证失败 (业务异常)
     * */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<HmsResponse<Void>> handleRecordNotFoundException(AuthenticationException ex) {
        log.error("The user fails to be authenticated.", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HmsResponse.error(ex.getHmsErrorCodeEnum()));
    }

    /*
     * 403 - 鉴权失败 (业务异常)
     * */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HmsResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("The user fails to be authorized.", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(HmsResponse.error(ex.getHmsErrorCodeEnum()));
    }

    /*
     * 404
     * */
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<HmsResponse<Void>> handleRecordNotFoundException(RecordNotFoundException e) {
        log.error("The requested resource could not be found.", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HmsResponse.error(e.getHmsErrorCodeEnum()));
    }

    /*
     * 业务异常兜底
     * */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<HmsResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("A business error occurred.", e);
        return ResponseEntity.badRequest().body(HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_004));
    }

    /*
     * 500 - all others exception handler
     * */
    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, @NonNull ServerHttpResponse response
    ) {
        if (body instanceof HmsResponse) {
            return body; // custom response;
        } else if (body instanceof Map) { // failed response
            Integer status = (Integer) ((Map<?, ?>) body).get("status");
            log.error("An error occurred. response body: {}; returnType: {}; selectedContentType: {}", body, returnType, selectedContentType);

            return HmsResponse.error(
                    HmsErrorCodeEnum.SYSTEM_ERROR_002.getCode(),
                    Optional.ofNullable(HmsHttpStatusEnum.getMessageByHttpCode(status)).orElse(HmsErrorCodeEnum.SYSTEM_ERROR_002.getMessage()),
                    null);
        }
        // FIXME: not sure what to do, but this is most likely an exception
        log.error("Unknown exception occurs. response body: {}; returnType: {}; selectedContentType: {}", body, returnType, selectedContentType);
        return HmsResponse.error(HmsErrorCodeEnum.SYSTEM_ERROR_001);
    }
}
