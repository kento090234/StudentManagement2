package raisetech.StudentManagement.controller.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourses;
import raisetech.StudentManagement.domain.StudentDetail;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static org.assertj.core.api.Assertions.assertThat;

class StudentConverterTest {

    private StudentConverter sut;

    @BeforeEach
    void before() {
        sut = new StudentConverter();
    }

    @Test
    void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
        Student student = createStudent();


        StudentCourses studentsCourses = new StudentCourses();
        studentsCourses.setId("1");
        studentsCourses.setStudentId("1");
        studentsCourses.setCourseName("Javaコース");
        studentsCourses.setCourseStartAt(LocalDateTime.now());
        studentsCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));

        List<Student> studentList = List.of(student);
        List<StudentCourses> studentCoursesList = List.of(studentsCourses);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList,studentCoursesList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentsCourseList()).isEqualTo(studentCoursesList);

        // assertThat(actual.get(0).getStudentsCourseList()).isEqualTo(studentsCourses);
    }

    private static Student createStudent() {
        Student student = new Student();
        student.setId("1");
        student.setName("山田太郎");
        student.setKanaName("ヤマダタロウ");
        student.setNickname("タロー");
        student.setEmail("taro@example.com");
        student.setArea("東京");
        student.setAge(Integer.parseInt("20"));
        student.setSex("男性");
        student.setRemark("");
        student.setDeleted(false);
        return student;
    }


    @Test
    void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること(){
       Student student = createStudent();

        StudentCourses studentsCourses = new StudentCourses();
        studentsCourses.setId("1");
        studentsCourses.setStudentId("2");
        studentsCourses.setCourseName("Javaコース");
        studentsCourses.setCourseStartAt(LocalDateTime.now());
        studentsCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));

        List<Student> studentList = List.of(student);
        List<StudentCourses> studentsCoursesList = List.of(studentsCourses);

        List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentsCoursesList);

        assertThat(actual.get(0).getStudent()).isEqualTo(student);
        assertThat(actual.get(0).getStudentsCourseList()).isEmpty();
    }

    public List<StudentDetail> convertStudentDateils (List<Student> studentList,
         List<StudentCourses> studentCoursesList) {
        List<StudentDetail> studentDetails = new ArrayList<>();
        studentList.forEach(student -> {
            StudentDetail studentDetail = new StudentDetail();
            studentDetail.setStudent(student);

            List<StudentCourses> convertStudentCoursesList = studentCoursesList.stream()
                    .filter(studentCourses -> student.getId().equals(studentCourses.getStudentId()))
                    .collect(Collectors.toList());

            studentDetail.setStudentsCourseList(convertStudentCoursesList);
            studentDetails.add(studentDetail);
        });
        return studentDetails;
    }
}