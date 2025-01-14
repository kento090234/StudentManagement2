package raisetech.StudentManagement.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.service.StudentService;

/**
 *  受講生検索や受講生登録、更新などを行う　REST　APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  /**
   * コンストラクター
   *
   * @param service　受講生サービス
   */
  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細一覧検索です。全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生詳細一覧（全件）
   */
// @GetMapping("/studentList")
//  public List<StudentDetail>getStudentList() {
//     return service.searchStudentList();
//  }

  /**
   * 受講生詳細検索です。IDに紐づく任意の受講生の情報を取得します。
   *
   * @param  id　受講生ID
   * @return 受講生詳細
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(
         @PathVariable @NotBlank @Pattern(regexp = "^\\d+$") String id) {
    return service.searchStudent(id);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
   StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok( responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。　キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail　受講生詳細
   * @return　実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<Map<String, String>> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);

    // レスポンスデータを Map に格納
    Map<String, String> response = new HashMap<>();
    response.put("message", "更新処理が成功しました。");

    // JSON 形式で返却
    return ResponseEntity.ok(response);
  }

  @GetMapping("/test")
List<StudentDetail> getStudentList() throws TestException {
    throw new TestException(
            "現在このAPIは利用できません。URLは「studentList」ではなく「students」を利用してください。"
    );
}
  }

