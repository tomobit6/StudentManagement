package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private String id;
  @NotBlank
  private String name;
  @NotBlank
  @Pattern(regexp = "^[ぁ-ん]+$", message = "ひらがなのみを入力してください。")
  private String ruby;
  private String nickname;
  @NotBlank
  @Email(message = "有効なメールアドレスを入力してください。")
  private String email;
  private String address;
  @Min(value = 10)
  @Max(value = 99)
  private Integer age;
  @Pattern(regexp = "^(男|女|その他)$", message = "「男」，「女」，「その他」のいずれかで入力してください。")
  private String gender;
  private String remark;
  private boolean isDeleted;


  public Student(String id, String name, String ruby, String nickname, String email, String address,
      Integer age, String gender, String remark, boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.ruby = ruby;
    this.nickname = nickname;
    this.email = email;
    this.address = address;
    this.age = age;
    this.gender = gender;
    this.remark = remark;
    this.isDeleted = isDeleted;
  }

  public Student(String name, String ruby, String nickname, String email, String address,
      Integer age, String gender, String remark, boolean isDeleted) {
    this.name = name;
    this.ruby = ruby;
    this.nickname = nickname;
    this.email = email;
    this.address = address;
    this.age = age;
    this.gender = gender;
    this.remark = remark;
    this.isDeleted = isDeleted;
  }

  public Student(String name, String ruby, String email) {
    this.ruby = ruby;
    this.name = name;
    this.email = email;
  }

  public Student(String name, String ruby, String nickname, String email, String address,
      Integer age,
      String gender) {
    this.name = name;
    this.ruby = ruby;
    this.nickname = nickname;
    this.email = email;
    this.address = address;
    this.age = age;
    this.gender = gender;
  }

  public Student() {
  }
}
