package raisetech.student.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細一覧検索_正常系_実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細検索_正常系_実行できて空の受講生が返ってくること() throws Exception {
    String id = "999";
    mockMvc.perform(get("/students/{id}", id))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudent(id);
  }

  @Test
  void 受講生詳細検索_異常系_受講生IDが空白だった場合に入力チェックが掛かること() {
    Student student = new Student();
    student.setId("");
    student.setName("仮名前");
    student.setRuby("かりなまえ");
    student.setNickname("仮");
    student.setEmail("karinamae@example.com");
    student.setAddress("高知県高知市");
    student.setAge(15);
    student.setGender("男");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
  }

  @Test
  void 受講生詳細登録_正常系_登録情報が適切に返ってくること() throws Exception {
    Student student = new Student();
    student.setName("仮名前");
    student.setRuby("かりなまえ");
    student.setEmail("karinamae@example.com");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("Javaコース");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    ObjectMapper objectMapper = new ObjectMapper();
    String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentDetailJson))
        .andExpect(status().isOk());

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  public void 受講生詳細登録_異常系_バリデーションエラー() throws Exception {
    mockMvc.perform(post("/registerStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"student\":{\"name\":\"\"}}"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細更新_正常系_更新情報が適切に返ってくること() throws Exception {
    Student student = new Student();
    student.setName("仮名前");
    student.setRuby("かりなまえ");
    student.setEmail("karinamae@example.com");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("Javaコース");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    ObjectMapper objectMapper = new ObjectMapper();
    String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentDetailJson))
        .andExpect(status().isOk());

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

  @Test
  public void 受講生詳細更新_異常系_バリデーションエラー() throws Exception {
    mockMvc.perform(put("/updateStudent")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"student\":\"email\":\"invalid-email\"}"))
        .andExpect(status().isBadRequest());

    verify(service, times(0)).updateStudent(any(StudentDetail.class));
  }
}
