package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@SuppressWarnings("NonAsciiCharacters")
@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void 正常系_受講生の全件検索が行えること() {
    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 正常系_受講生コースの全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(8);
  }

  @Test
  void 正常系_受講生IDに紐づく受講生の検索が行えること() {
    String id = "1";
    String expectName = "北海正己";

    Student actual = sut.searchStudent(id);

    assertThat(actual.getName()).isEqualTo(expectName);
  }

  @Test
  void 異常系_DBに存在しない受講生IDが指定された場合にnullを返すこと() {
    String id = "99";

    Student actual = sut.searchStudent(id);

    assertThat(actual).isNull();
  }

  @Test
  void 正常系_受講生IDに紐づいた受講生コースの検索が行えること() {
    Student student = new Student();
    student.setId("2");

    String expectFirstCourseName = "Webマーケティングコース";
    String expectLastCourseName = "バックエンド開発";

    List<StudentCourse> actual = sut.searchStudentCourse(student.getId());

    assertThat(actual.getFirst().getCourseName()).isEqualTo(expectFirstCourseName);
    assertThat(actual.getLast().getCourseName()).isEqualTo(expectLastCourseName);
  }

  @Test
  void 正常系_受講生の登録が行えること() {
    Student student = createStudent();
    sut.insertStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  @Rollback(false)
  void 正常系_受講生コースの登録が行えること() {
    Student student = createStudent();
    sut.insertStudent(student);

    StudentCourse studentCourse = createStudentCourse(student);
    sut.insertStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(9);
  }

  // 受講生データを作成するメソッド
  private Student createStudent() {
    Student student = new Student();
    student.setName("仮名前");
    student.setRuby("かりなまえ");
    student.setNickname("仮");
    student.setEmail("karinamae@example.com");
    student.setAddress("高知県高知市");
    student.setAge(15);
    student.setGender("男");
    return student;
  }

  // 受講生のコースデータを作成するメソッド
  private StudentCourse createStudentCourse(Student student) {
    StudentCourse studentCourse = new StudentCourse();
    LocalDate now = LocalDate.now();
    studentCourse.setId(student.getId());
    studentCourse.setStudentId(student.getId());
    studentCourse.setCourseName("Javaコース");
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
    return studentCourse;
  }

  @Test
  void 正常系_受講生の更新が行えていること() {
    String id = "2";

    Student student = new Student();
    student.setId(id);
    // id:2の受講生が結婚して姓が変わり、引っ越した想定
    student.setName("鈴木花子");
    student.setRuby("すずきはなこ");
    student.setEmail("hanako.suzuki@example.com");
    student.setAddress("神奈川県横須賀市");

    sut.updateStudent(student);

    Student actual = sut.searchStudent(id);

    assertThat(actual.getName()).isEqualTo(student.getName());
    assertThat(actual.getRuby()).isEqualTo(student.getRuby());
    assertThat(actual.getEmail()).isEqualTo(student.getEmail());
    assertThat(actual.getAddress()).isEqualTo(student.getAddress());
  }

  @Test
  void 正常系_受講生IDに紐づいた受講生コースの更新が行えていること() {
    String studentId = "1";

    StudentCourse studentCourse = new StudentCourse();
    LocalDate now = LocalDate.now();
    studentCourse.setId("1");
    studentCourse.setStudentId(studentId);
    // id:1の受講生がコースが合わず今のコースを変更する想定
    studentCourse.setCourseName("Webデザインコース");
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

    assertThat(actual.get(0).getCourseName()).isEqualTo(studentCourse.getCourseName());
  }
}
