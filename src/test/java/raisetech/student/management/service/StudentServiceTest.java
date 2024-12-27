package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
  void 受講生詳細一覧検索_リポジトリとコンバーターの処理が適切に呼び出されていること() {
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();

    // モックの設定
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    // 実行
    sut.searchStudentList();

    // 検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生詳細一覧検索_戻り値が正しいこと() {
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
    assertEquals(expected, actual);
  }

  @Test
  void 受講生詳細検索_リポジトリの処理が適切に呼び出されていること()
      throws NotFoundException {
    // 事前準備
    String id = "1";
    Student student = new Student();
    List<StudentCourse> studentCourse = new ArrayList<>();

    // モックの設定
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourse);

    // 実行
    sut.searchStudent(id);

    // 検証
    verify(repository, times(1)).searchStudent(id);
    verify(repository, times(1)).searchStudentCourse(student.getId());
  }

  @Test
  void 受講生詳細検索_戻り値が正しいこと()
      throws NotFoundException {
    // 事前準備
    String id = "1";

    Student student = new Student();
    student.setId(id);
    student.setName("仮名前");
    student.setRuby("かりなまえ");
    student.setNickname("仮");
    student.setEmail("karinamae@example.com");
    student.setAddress("高知県高知市");
    student.setAge(15);
    student.setGender("男");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setId("1");
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseName("Javaコース");
    studentCourse.setStartDate(LocalDate.of(2024, 1, 1));
    studentCourse.setEndDate(LocalDate.of(2024, 12, 31));

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);

    // モックの設定
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourseList);

    // 実行
    StudentDetail actual = sut.searchStudent(id);

    // 検証
    assertEquals(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細検索_学生が見つからない場合正しく例外がスローされること()
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
  void 受講生詳細登録_リポジトリの処理が適切に呼び出せていること() {
    // 事前準備
    Student student = new Student();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // モックの設定
    StudentService spy = spy(sut);

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    verify(repository, times(1)).insertStudent(studentDetail.getStudent());
    verify(spy, times(studentCourseList.size())).initStudentCourse(any(StudentCourse.class),
        eq(student));
    verify(repository, times(studentCourseList.size())).insertStudentCourse(
        any(StudentCourse.class));
  }

  @Test
  void 受講生詳細登録_戻り値が正しいこと() {
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
    studentCourse.setId("1");
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseName("Javaコース");
    studentCourse.setStartDate(LocalDate.of(2024, 1, 1));
    studentCourse.setEndDate(LocalDate.of(2024, 12, 31));

    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);

    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    // 実行
    StudentDetail actual = sut.registerStudent(studentDetail);

    // 検証
    assertEquals(studentDetail, actual);
  }

  @Test
  void 受講生コース情報登録_適切に情報が渡されていること() {
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
  void 受講生詳細更新_リポジトリの処理が適切に呼び出せていること() {
    // 事前準備
    StudentDetail studentDetail = new StudentDetail();

    Student student = new Student();
    studentDetail.setStudent(student);

    StudentCourse studentCourse = new StudentCourse();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);
    studentDetail.setStudentCourseList(studentCourseList);

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    verify(repository, times(1)).updateStudent(studentDetail.getStudent());
    verify(repository, times(1)).updateStudentCourse(studentCourse);
  }
}
