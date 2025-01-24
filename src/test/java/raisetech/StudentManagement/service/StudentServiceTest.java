package raisetech.StudentManagement.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @Mock
    private StudentConverter converter;

    private StudentService sut;


    @BeforeEach
    void before(){
        sut = new StudentService(repository, converter);

    }

    @Test
    void 受講生詳細の一覧検索＿リポジトリとコンバーターの処理が適切に呼び出せていること() {

        List<Student> studentList = new ArrayList<>();
        List<StudentsCourses> studentsCoursesList = new ArrayList<>();
        Mockito.when(repository.search()).thenReturn(studentList);
        Mockito.when(repository.searchStudentCoursesList()).thenReturn(studentsCoursesList);

        sut.searchStudentList();

        Mockito.verify(repository, times(1)).search();
        Mockito.verify(repository, times(1)).searchStudentCoursesList();
        Mockito.verify(converter, times(1)).convertStudentDetails(studentList, studentsCoursesList);
    }
    @Test
    void 受講生詳細の検索＿リポジトリの処理が適切に呼び出せていること(){
     String id = "999";
     Student student = new Student();
     student.setId(id);
     Mockito.when(repository.searchStudent(id)).thenReturn(student);
     Mockito.when(repository.searchStudentsCourses(id)).thenReturn(new ArrayList<>());

     StudentDetail expected = new StudentDetail(student, new ArrayList<>());

     StudentDetail actunal = sut.searchStudent(id);

     Mockito.verify(repository, times(1)).searchStudent(id);
     Mockito.verify(repository, times(1)).searchStudentsCourses(id);
     assertEquals(expected.getStudent().getId(),actunal.getStudent().getId());
        }

    @Test
     void 受講生詳細の登録＿リポジトリの処理が適切に呼び出せていること() {
        Student student = new Student();
        StudentsCourses studentsCourses = new StudentsCourses();
        List<StudentsCourses> studentsCoursesList = List.of(studentsCourses);
        StudentDetail studentDetail = new StudentDetail(student, studentsCoursesList);

        sut.registerStudent(studentDetail);

        Mockito.verify(repository, times(1)).registerStudent(student);
        Mockito.verify(repository, times(1)).registerStudentsCourses(studentsCourses);
    }

    @Test
    void 受講生詳細の登録＿初期化処理が行われていること() {
     String id ="999";
     Student student = new Student();
     student.setId(id);
     StudentsCourses studentsCourses = new StudentsCourses();

     sut.initStudentsCourse(studentsCourses, student.getId());

     assertEquals(id, studentsCourses.getStudentId());
     assertEquals(LocalDateTime.now().getHour(), studentsCourses.getCourseEndAt().getHour());
     assertEquals(LocalDateTime.now().plusYears(1).getYear(), studentsCourses.getCourseEndAt().getYear());

    }

    @Test
    void 受講生詳細の更新＿リポジトリの処理が適切に呼び出せていること(){
        Student student = new Student();
        StudentsCourses studentsCourses = new StudentsCourses();
        List<StudentsCourses> studentsCoursesList = List.of(studentsCourses);
        StudentDetail studentDetail = new StudentDetail(student, studentsCoursesList);

        sut.updateStudent(studentDetail);

        Mockito.verify(repository, times(1)).updateStudent(student);
        Mockito.verify(repository, times(1)).updateStudentsCourses(studentsCourses);


    }












}






