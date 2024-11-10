package raisetech.student.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Handler {

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<String> handleNotFoundException(NotFoundException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body((ex.getMessage()));
  }

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<String> handleInvalidDataException(InvalidDataException ex){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("予期しないエラーが発生しました: " + ex.getMessage());
  }
}
