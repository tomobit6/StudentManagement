package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    List<Student> studentList = repository.search();

    List<Student> studentsAged25To29 = studentList.stream()
        .filter(student -> student.getAge() >= 25 && student.getAge() < 30)
        .toList();
    return studentsAged25To29;
  }

  public List<StudentCourse> searchStudentCourseList() {
    List<StudentCourse> studentCourseList = repository.searchStudentCourse();

    List<StudentCourse> webCourse = studentCourseList.stream()
        .filter(studentCourse -> studentCourse.getCourseName().equals("ウェブ開発基礎"))
        .toList();
    return webCourse;
  }
}
