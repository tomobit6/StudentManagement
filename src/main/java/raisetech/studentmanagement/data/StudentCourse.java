package raisetech.studentmanagement.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private String id;
  private String studentId;
  @NotBlank
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
}
