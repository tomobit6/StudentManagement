package raisetech.studentmanagement.repository;
//データベースを操作するものだと思ったらいい

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourses;

@Mapper
public interface StudentRepository {


  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentCourses> searchStudentCourses();

  // students()の中身はテーブルのカラム名　valuesの後はstudentのデータ名
  @Insert("INSERT INTO students(name,ruby,nickname,email,address,age,gender,remark) "
      + "VALUES(#{name},#{ruby},#{nickname},#{email},#{address},#{age},#{gender},#{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertStudent(Student student);

  @Insert("INSERT INTO students_courses(student_id,course_name,start_date,end_date) "
      + "VALUES(LAST_INSERT_ID(),#{courseName},#{startDate},#{endDate})")
  @Options(useGeneratedKeys = true,keyProperty = "id")
  void insertStudentCourse(StudentCourses studentCourses);
}
