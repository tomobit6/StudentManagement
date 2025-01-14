package raisetech.student.management.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Handler {

  @ExceptionHandler(NotReadyException.class)
  public ResponseEntity<Map<String, String>> handleNotReadyException(NotReadyException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("エラーメッセージ", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("エラーメッセージ", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler
  public ResponseEntity<List<Map<String, String>>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    List<Map<String, String>> response = new ArrayList<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      Map<String, String> errorDetail = new HashMap<>();
      errorDetail.put("フィールド名", error.getField());
      errorDetail.put("エラーメッセージ", error.getDefaultMessage());
      response.add(errorDetail);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(
      ConstraintViolationException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("エラーメッセージ", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}

