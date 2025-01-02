package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.NotFoundException;
import raisetech.student.management.repository.StudentRepository;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細一覧検索_正常系_リポジトリとコンバーターの処理が適切に呼び出されていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    // モックの設定
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

    // 実行
    List<StudentDetail> actual = sut.searchStudentList();

    // 検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);

    assertEquals(expected, actual);
  }

  @Test
  void 受講生詳細検索_リポジトリの処理が適切に呼び出されていること()
      throws NotFoundException {
    // 事前準備
    String id = "1";
    Student student = new Student();

    StudentCourse studentCourse = new StudentCourse();

    List<StudentCourse> studentCourseList = List.of(studentCourse);

    // モックの設定
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourseList);

    // 実行
    StudentDetail actual = sut.searchStudent(id);

    // 検証
    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(student.getId());

    assertEquals(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細検索_異常系_学生が見つからない場合正しく例外がスローされること()
      throws NotFoundException {
    // 事前準備
    String id = "1";

    // モックの設定
    when(repository.searchStudent(id)).thenReturn(null);

    // 実行
    NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
      sut.searchStudent(id);
    });

    // 検証
    assertEquals("ID:1の受講生は存在しません。", thrown.getMessage());
  }

  @Test
  void 受講生詳細登録_正常系_リポジトリの処理が適切に呼び出せていること() {
    // 事前準備
    Student student = new Student();
    student.setId("1");
    student.setName("仮名前");
    student.setRuby("かりなまえ");
    student.setNickname("仮");
    student.setEmail("karinamae@example.com");
    student.setAddress("高知県高知市");
    student.setAge(15);
    student.setGender("男");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId(student.getId());
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseName("Javaコース");
    studentCourse.setStartDate(LocalDate.of(2024, 1, 1));
    studentCourse.setEndDate(LocalDate.of(2024, 12, 31));

    List<StudentCourse> studentCourseList = List.of(studentCourse);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    StudentDetail actual = sut.registerStudent(studentDetail);

    // 検証
    verify(repository, times(1)).insertStudent(studentDetail.getStudent());
    verify(repository, times(studentCourseList.size())).insertStudentCourse(
        any(StudentCourse.class));

    assertEquals(studentDetail, actual);
  }

  @Test
  void 受講生コース情報登録_正常系_適切に情報が渡されていること() {
    // 事前準備
    StudentCourse studentCourse = new StudentCourse();
    String studentId = "1";
    LocalDate fixDate = LocalDate.of(2024, 12, 27);

    // モックの設定
    Student spy = spy(Student.class);
    when(spy.getId()).thenReturn(studentId);

    try (MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class)) {
      mockedStatic.when(LocalDate::now).thenReturn(fixDate);

      // 実行
      sut.initStudentCourse(studentCourse, spy);

      // 検証
      assertEquals(studentId, studentCourse.getStudentId());
      assertEquals(fixDate, studentCourse.getStartDate());
      assertEquals(fixDate.plusYears(1), studentCourse.getEndDate());
    }
  }

  @Test
  void 受講生詳細更新_正常系_リポジトリの処理が適切に呼び出せていること() {
    // 事前準備
    Student student = new Student();

    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(studentCourseList.size())).updateStudentCourse(
        any(StudentCourse.class));
  }
}
