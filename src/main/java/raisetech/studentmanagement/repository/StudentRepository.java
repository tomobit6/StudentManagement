package raisetech.studentmanagement.repository;
//データベースを操作するものだと思ったらいい

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param id　受講生ID
   * @return 受講生
   */
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student  searchStudent(String id);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報を検索します。
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * 受講生を新規登録します。
   * IDに関しては自動採番を行う。
   * @param student 受講生
   */
  @Insert("INSERT INTO students(name,ruby,nickname,email,address,age,gender,remark,is_deleted) "
      + "VALUES(#{name}, #{ruby}, #{nickname}, #{email}, #{address}, #{age}, #{gender}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う。
   *
   * @param studentCourse 受講生コース情報
   */
  @Insert("INSERT INTO students_courses(student_id,course_name,start_date,end_date) "
      + "VALUES(LAST_INSERT_ID(), #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true,keyProperty = "id")
  void insertStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   * @param student 受講生
   */
  @Update("UPDATE students SET name = #{name}, ruby = #{ruby}, nickname = #{nickname}, email = #{email},"
      + "address = #{address}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted}  WHERE id = #{id}")
  void updateStudent(Student student);

  /**
   * 受講生コース情報のコース名を更新します。
   *
   * @param studentCourses 受講生コース情報
   */
  @Update("UPDATE students_courses SET course_name = #{courseName} WHERE id = #{id}")
  void updateStudentCourse(StudentCourse studentCourses);
}
