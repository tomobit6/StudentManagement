package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    assertThat(actual.get(0).getName()).isEqualTo("北海正己");
    assertThat(actual.get(1).getName()).isEqualTo("佐藤花子");
    assertThat(actual.get(2).getName()).isEqualTo("山田太郎");
    assertThat(actual.get(3).getName()).isEqualTo("川島梅子");
    assertThat(actual.get(4).getName()).isEqualTo("田中検事");
    assertThat(actual.get(5).getName()).isEqualTo("東北清子");
  }

  @Test
  void 正常系_受講生コースの全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(8);
    assertThat(actual.get(0).getCourseName()).isEqualTo("web開発基礎");
    assertThat(actual.get(1).getCourseName()).isEqualTo("Webマーケティングコース");
    assertThat(actual.get(2).getCourseName()).isEqualTo("バックエンド開発");
    assertThat(actual.get(3).getCourseName()).isEqualTo("データベース入門");
    assertThat(actual.get(4).getCourseName()).isEqualTo("ウェブ開発基礎");
    assertThat(actual.get(5).getCourseName()).isEqualTo("ウェブ開発基礎");
    assertThat(actual.get(6).getCourseName()).isEqualTo("フロントエンド開発");
    assertThat(actual.get(7).getCourseName()).isEqualTo("データベース設計");
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
    String studentId = "2";
    Student student = Mockito.mock(Student.class);
    when(student.getId()).thenReturn(studentId);

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
    Student student = new Student("仮名前", "かりなまえ", "カリ", "karinamae@example.com",
        "高知県高知市", 15, "男", "", false);
    return student;
  }

  // 受講生のコースデータを作成するメソッド
  private StudentCourse createStudentCourse(Student student) {
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(student.getId(), "Javaコース");
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
    return studentCourse;
  }

  @Test
  void 正常系_受講生の更新が行えていること() {
    String id = "2";

    Student student = new Student("佐藤花子", "さとうはなこ", "ハナちゃん",
        "hanako.sato@example.com", "神奈川県横浜市", 25, "女", "", false);

    Student spy = spy(student);
    when(spy.getId()).thenReturn(id);

    // 佐藤さんが結婚して姓が変わり、引っ越した想定
    spy.setName("鈴木花子");
    spy.setRuby("すずきはなこ");
    spy.setEmail("hanako.suzuki@example.com");
    spy.setAddress("神奈川県横須賀市");

    sut.updateStudent(spy);

    Student actual = sut.searchStudent(id);

    assertThat(actual.getName()).isEqualTo(spy.getName());
    assertThat(actual.getRuby()).isEqualTo(spy.getRuby());
    assertThat(actual.getEmail()).isEqualTo(spy.getEmail());
    assertThat(actual.getAddress()).isEqualTo(spy.getAddress());
  }

  @Test
  void 正常系_受講生IDに紐づいた受講生コースの更新が行えていること() {
    String studentId = "1";
    StudentCourse studentCourse = sut.searchStudentCourse(studentId).get(0);

    LocalDate now = LocalDate.now();

    // id:1の受講生がコースが合わず今のコースを変更する想定
    studentCourse.setCourseName("Webデザインコース");
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));

    sut.updateStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

    assertThat(actual.get(0).getCourseName()).isEqualTo(studentCourse.getCourseName());
  }
}
