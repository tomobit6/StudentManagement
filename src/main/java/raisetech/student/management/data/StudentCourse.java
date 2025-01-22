package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  private String id;
  private String studentId;
  @NotBlank
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;


  public StudentCourse(String id, String studentId, String courseName, LocalDate startDate,
      LocalDate endDate) {
    this.id = id;
    this.studentId = studentId;
    this.courseName = courseName;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public StudentCourse(String studentId, String courseName, LocalDate startDate,
      LocalDate endDate) {
    this.studentId = studentId;
    this.courseName = courseName;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public StudentCourse(String studentId, String courseName) {
    this.studentId = studentId;
    this.courseName = courseName;
  }

  public StudentCourse() {
  }
}
