package raisetech.studentmanagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String id;
  private String name;
  private String ruby;
  private String nickname;
  private String email;
  private String address;
  private Integer age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}
