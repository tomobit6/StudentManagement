package raisetech.studentmanagement.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourses;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourses> studentCourses = service.searchStudentCoursesList();
    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourses> getStudentCoursesList() {
    return service.searchStudentCoursesList();
  }


  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();

    Student student = new Student();
    student.setGender("");
    student.setAge(null);
    studentDetail.setStudent(student);

    StudentCourses studentCourse = new StudentCourses();
    studentCourse.setCourseName("");
    studentDetail.setStudentCourse(new ArrayList<>(List.of(studentCourse)));
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      System.out.println("エラー");
      return "registerStudent";
    }
    System.out.println(studentDetail.getStudent().getName() + "さんが登録されました。");
    // 新規受講生情報を登録する処理を実装する。
    Student student = studentDetail.getStudent();
    // registerStudentメソッドを呼び出し、新規の受講生情報を変数studentとして渡す
    service.registerStudent(student);
    // コース情報も一緒に登録できるように実装する。コースは単体で良い。
    return "redirect:/studentList";
  }
}
