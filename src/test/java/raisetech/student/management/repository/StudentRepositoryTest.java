package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
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
    List<Student> expected = Arrays.asList(
        new Student("1", "北海正己", "ほっかいまさみ", "ホッカイくん", "todoroki@example.com",
            "北海道札幌市", 22, "男", null, false),
        new Student("2", "佐藤花子", "さとうはなこ", "ハナちゃん", "hanako.sato@example.com",
            "神奈川県横浜市", 25, "女", null, false),
        new Student("3", "山田太郎", "やまだたろう", "タロちゃん", "taro.yamada@example.com",
            "大阪府大阪市", 30, "その他", null, false),
        new Student("4", "川島梅子", "かわしまうめこ", "ウメちゃん", "ume@example.com",
            "山口県山口市",
            18, "女", null, false),
        new Student("5", "田中検事", "たなかけんじ", "ケンちゃん", "kenji@example.com",
            "徳島県徳島市",
            36, "男", null, false),
        new Student("6", "東北清子", "とうほくきよこ", "キヨちゃん", "kiyo@example.com",
            "宮城県仙台市",
            26, "女", null, false)
    );

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void 正常系_受講生コースの全件検索が行えること() {
    List<StudentCourse> expected = Arrays.asList(
        new StudentCourse("1", "1", "web開発基礎", LocalDate.parse("2024-11-03"),
            LocalDate.parse("2025-11-03")),
        new StudentCourse("2", "2", "Webマーケティングコース", LocalDate.parse("2024-11-10"),
            LocalDate.parse("2025-11-10")),
        new StudentCourse("3", "2", "バックエンド開発", LocalDate.parse("2024-01-10"),
            LocalDate.parse("2024-03-10")),
        new StudentCourse("4", "3", "データベース入門", LocalDate.parse("2024-02-15"),
            LocalDate.parse("2024-04-15")),
        new StudentCourse("5", "4", "ウェブ開発基礎", LocalDate.parse("2024-03-01"),
            LocalDate.parse("2024-05-01")),
        new StudentCourse("6", "5", "ウェブ開発基礎", LocalDate.parse("2024-03-11"),
            LocalDate.parse("2024-05-10")),
        new StudentCourse("7", "6", "フロントエンド開発", LocalDate.parse("2024-10-19"),
            LocalDate.parse("2025-10-19")),
        new StudentCourse("8", "6", "データベース設計", LocalDate.parse("2024-10-25"),
            LocalDate.parse("2025-10-25"))
    );

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(8);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
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

    String expectFirstCourseName = "Webマーケティングコース";
    String expectLastCourseName = "バックエンド開発";

    List<StudentCourse> actual = sut.searchStudentCourse(studentId);

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
