package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

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
}
