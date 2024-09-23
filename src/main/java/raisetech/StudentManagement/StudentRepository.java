package raisetech.StudentManagement;
//データベースを操作するものだと思ったらいい

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  //名前から学生情報の取得
  @Select("SELECT * FROM student WHERE name = #{name}")
  Student searchByName(String name);

  //全ての学生情報の取得
  @Select("SELECT * FROM student")
  List<Student> findAllStudents();

  //学生情報の登録
  @Insert("INSERT student (name,age) values(#{name},#{age})")
  void registerStudent(String name, int age);

  //学生情報の更新
  @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  void updateStudent(String name, int age);

  //学生情報を削除
  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);
}
