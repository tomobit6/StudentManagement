package raisetech.student.management.controller.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

@SuppressWarnings("NonAsciiCharacters")
class StudentConverterTest {

  @Test
  void 正常系_受講生や受講生コース情報が正しく受講生詳細に変換できていること() {
    Student student1 = new Student();
    student1.setId("777");
    Student student2 = new Student();
    student2.setId("888");
    Student student3 = new Student();
    student3.setId("999");

    List<Student> studentList = List.of(student1, student2, student3);

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setStudentId(student1.getId());
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setStudentId(student1.getId()); // student1はコース情報2つと仮定
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setStudentId(student2.getId());
    StudentCourse studentCourse4 = new StudentCourse();
    studentCourse4.setStudentId(student3.getId());

    List<StudentCourse> studentCourses = List.of(studentCourse1, studentCourse2,
        studentCourse3, studentCourse4);

    List<StudentDetail> studentDetails = new ArrayList<>();

    for (Student student : studentList) {
      StudentDetail studentDetail = new StudentDetail();

      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourses = new ArrayList<>();
      for (StudentCourse studentCourse : studentCourses) {
        if (student.getId().equals(studentCourse.getStudentId())) {
          convertStudentCourses.add(studentCourse);
        }
      }
      studentDetail.setStudentCourseList(convertStudentCourses);

      studentDetails.add(studentDetail);
    }
    assertAll(
        () -> {
          assertEquals("777", studentDetails.get(0).getStudent().getId());
          assertEquals(2, studentDetails.get(0).getStudentCourseList().size());
        },
        () -> {
          assertEquals("888", studentDetails.get(1).getStudent().getId());
          assertEquals(1, studentDetails.get(1).getStudentCourseList().size());
        },
        () -> {
          assertEquals("999", studentDetails.get(2).getStudent().getId());
          assertEquals(1, studentDetails.get(2).getStudentCourseList().size());
        }
    );
    assertEquals(3, studentDetails.size());
  }
}
