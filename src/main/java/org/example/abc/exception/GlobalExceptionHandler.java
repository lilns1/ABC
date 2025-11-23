// 文件路径: src/main/java/org/example/abc/exception/GlobalExceptionHandler.java

package org.example.abc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ 处理 @Valid 校验失败（请求体字段校验）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage() != null ?
                        error.getDefaultMessage() : "字段校验失败")
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // 2️⃣ 处理方法参数校验失败（如 @Email String email）
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    // 3️⃣ ✅ 新增：专门处理支付业务异常
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<String> handlePaymentException(PaymentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // 4️⃣ 保留你原有的 IllegalArgumentException 处理
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    // 5️⃣ 兜底：未预期的异常（返回 500）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception e) {
        // 生产环境建议记录日志，不要打印堆栈给前端
        // e.printStackTrace(); // 开发时可临时开启
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器内部错误");
    }

    // ⚠️ 注意：不再捕获通用的 RuntimeException！
    // 因为 PaymentException、IllegalArgumentException 都是它的子类，
    // 如果先被 RuntimeException 捕获，就无法进入上面更具体的处理器。
}