package raisetech.studentmanagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourses {

  private String id;
  private String studentId;
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
}
