package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うServiceです。
 * 受講生の検索や登録：更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   *　受講生詳細の一覧検索です。全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentsCourses> studentsCoursesList = repository.searchStudentCoursesList();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * 受講生詳細検索です。IDに紐づく受講生情報を取得したあと、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(String id){
    Student student = repository.searchStudent(id);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses(student.getId());
    return new StudentDetail(student, studentsCourses);
  }

  /**
   * 受講生詳細の登録を行います。受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   *  @param studentDetail　受講生詳細
   * @return　受講生登録を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    
    repository.registerStudent(student);
      studentDetail.getStudentsCourseList().forEach(studentsCourse -> {
        initStudentsCourse(studentsCourse, student);
      });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際に初期情報を設定する。
   *
   * @param studentsCourse　受講生コース情報
   * @param student　受講生
   */
  private void initStudentsCourse(StudentsCourses studentsCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentsCourse.setStudentId(student.getId());
    studentsCourse.setCourseStartAt(now);
    studentsCourse.setCourseEndAt(now.plusYears(1));
    repository.registerStudentsCourses(studentsCourse);
  }

  /**
   * 受講生詳細の更新を行います。　受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail　受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
      studentDetail.getStudentsCourseList().
              forEach(studentsCourse -> repository.updateStudentsCourses(studentsCourse));
  }
}
