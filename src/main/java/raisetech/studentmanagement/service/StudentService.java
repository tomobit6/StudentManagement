package raisetech.studentmanagement.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourses;
import raisetech.studentmanagement.domain.StudentDetail;
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
    return repository.searchStudentCoursesList();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // repositoryのinsertStudentメソッドを呼び出し、新規の受講生情報を渡す。
    repository.insertStudent(studentDetail.getStudent());
    // TODO:コース情報管理を扱う。
    for(StudentCourses studentCourses :studentDetail.getStudentCourses()){
      studentCourses.setStudentId(studentDetail.getStudent().getId());
      studentCourses.setStartDate(LocalDate.now());
      studentCourses.setEndDate(LocalDate.now().plusYears(1));
    repository.insertStudentCourse(studentCourses);
    }
  }

  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentCourses> studentCourses = repository.searchStudentCourses(student.getId());
    StudentDetail studentDetail =new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourses);
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail){
    repository.updateStudent(studentDetail.getStudent());
    for(StudentCourses studentCourses :studentDetail.getStudentCourses()){
      repository.updateStudentCourse(studentCourses);
    }
  }
}
