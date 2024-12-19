package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.NotFoundException;
import raisetech.student.management.exception.TestException;
import raisetech.student.management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;



  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 全件検索を行うので、条件指定は行わいなものになります。
   *
   * @return 受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索",description = "受講生の一覧を検索します。")
  @GetMapping("/students")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return 受講生
   */
  @Operation(summary = "受講生検索",description = "IDに紐づく任意の受講生を検索します。")
  @ApiResponse(responseCode = "404",description = "ユーザーが見つかりません。")
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(
      @Parameter(description = "受講生ID",required = true)
      @PathVariable @Size(min=1,max= 3) String id)
      throws NotFoundException {
    return service.searchStudent(id);
  }

  /**
   * わざと例外処理を発生させるためのURLです。
   *
   * @throws TestException
   */
  @Operation(summary = "受講生の就職先一覧検索",description = "受講生の就職先一覧を検索します。")
  @ApiResponse(responseCode = "400",description = "リクエストが無効です。")
  @GetMapping("/jobs")
  public StudentDetail getJobs() throws TestException {
    throw  new TestException("受講生の就職先情報は現在準備中です。");
  }

  /**
   * 受講生詳細の登録を行います。
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録",description = "受講生を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail>registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
   StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。
   * キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新",description = "受講生を更新します。")
  @PutMapping("/updateStudent")
  public ResponseEntity<String>updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が完了しました。");
  }
}
