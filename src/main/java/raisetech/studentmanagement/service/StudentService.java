package raisetech.studentmanagement.service;

import java.util.List;
import org.springframework.stereotype.Service;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourses;
import raisetech.studentmanagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentCourses> searchStudentCoursesList() {
    return repository.searchStudentCourses();
  }

  public void registerStudent(Student student) {
    // repositoryのinsertStudentメソッドを呼び出し、新規の受講生情報を渡す。
    repository.insertStudent(student);
  }
}
