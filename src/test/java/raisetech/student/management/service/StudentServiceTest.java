package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Not;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.propertyeditors.LocaleEditor;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.NotFoundException;
import raisetech.student.management.repository.StudentRepository;

// Mock化 stub
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach // 各テストメソッドの前に必ず実行される。@BeforeAllはテストクラスに1回。
  void before(){
    sut =new StudentService(repository,converter); // sutはテスト対象という意味
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出されていること() {
    // 事前準備
    List<Student>studentList=new ArrayList<>();
    List<StudentCourse>studentCourseList = new ArrayList<>();

    // モックの設定
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    // 実行
    sut.searchStudentList(); //actualはテストの検証をする対象

    // 検証
    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList,studentCourseList);

    // 後処理
    // 例えば、DBを元に戻す。
  }

  @Test
  void 受講生詳細検索_リポジトリの処理が適切に呼び出されていること()
      throws NotFoundException {
    // 事前準備
    String id = "1"; // 引数はテストメソッドには、持たせないルール
    Student student = new Student();
    List<StudentCourse>studentCourse =new ArrayList<>();

    // モックの設定
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourse);

    // 実行
    sut.searchStudent(id);

    // 検証
    verify(repository,times(1)).searchStudent(id);
    verify(repository,times(1)).searchStudentCourse(student.getId());
  }

  @Test
  void 受講生詳細検索_返り値が正しいこと() throws NotFoundException {
    // 事前準備
    String id = "1";
    Student student = new Student();
    List<StudentCourse> studentCourse = new ArrayList<>();

    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourse);

    // 実行
    StudentDetail actual = sut.searchStudent(id);

    // 検証
    assertEquals(student,actual.getStudent());
    assertEquals(studentCourse,actual.getStudentCourseList());
  }

  @Test
  void 受講生詳細検索_学生が見つからない場合正しく例外がスローされること()throws NotFoundException {
    // 事前準備
    String id ="1";

    // モックの設定
    when(repository.searchStudent(id)).thenReturn(null);

    // 検証
    NotFoundException thrown = assertThrows(NotFoundException.class,() -> {sut.searchStudent(id);});

    assertEquals("ID:1の受講生は存在しません。", thrown.getMessage());

  }

  @Test
  void 受講生詳細登録_リポジトリの処理が適切に呼び出せていること(){
    // 事前準備
    StudentDetail studentDetail = new StudentDetail();
    Student student = new Student();
    StudentCourse studentCourse = new StudentCourse();

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    verify(repository,times(1)).insertStudent(student);
    verify(repository,times(1)).insertStudentCourse(studentCourse);
  }

  @Test
  void 受講生詳細登録_返り値が正しいこと(){

  }

  @Test
  void 受講生コース情報登録_適切に情報が渡されていること(){
    // 事前準備
    StudentCourse studentCourse = new StudentCourse();
    Student student = mock(Student.class);
    LocalDate fixDate = LocalDate.of(2024,12,19);

    // モックの設定
    String studentId = "1";
    when(student.getId()).thenReturn(studentId);

    // 実行
    sut.initStudentCourse(studentCourse,student);

    // 検証
    assertEquals(studentId,studentCourse.getStudentId());
    assertEquals(fixDate,studentCourse.getStartDate());
    assertEquals(fixDate.plusYears(1),studentCourse.getEndDate());
  }

  @Test
  void 受講生詳細更新_リポジトリの処理が適切に呼び出せていること(){
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
    verify(repository,times(1)).updateStudent(studentDetail.getStudent());
    verify(repository,times(1)).updateStudentCourse(studentCourse);
  }
}
