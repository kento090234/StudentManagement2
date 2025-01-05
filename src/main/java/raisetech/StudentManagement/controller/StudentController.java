package raisetech.StudentManagement.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 *  受講生検索や受講生登録、更新などを行う　REST　APIとして受け付けるControllerです。
 */
@RestController
public class StudentController {

  private StudentService service;

  /**
   * コントラクター
   *
   * @param service　受講生サービス
   */
  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生一覧（全件）
   */
  @GetMapping("/studentList")
  public List<StudentDetail>getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param  id　受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable String id) {
    return service.searchStudent(id);
  }

  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@RequestBody StudentDetail studentDetail) {
   StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok( responseStudentDetail);
  }

  @PostMapping("/updateStudent")
  public ResponseEntity<Map<String, String>> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);

    // レスポンスデータを Map に格納
    Map<String, String> response = new HashMap<>();
    response.put("message", "更新処理が成功しました。");

    // JSON 形式で返却
    return ResponseEntity.ok(response);
  }
}
// @PostMapping("/updateStudent")
//  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
//    service.updateStudent(studentDetail);
//    return ResponseEntity.ok("更新処理が成功しました。");
//  }

//  @GetMapping("/newStudent")
//  public String newStudent(Model model) {
//    StudentDetail studentDetail = new StudentDetail();
//    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
//    model.addAttribute("studentDetail", studentDetail);
//    return "registerStudent";
//  }