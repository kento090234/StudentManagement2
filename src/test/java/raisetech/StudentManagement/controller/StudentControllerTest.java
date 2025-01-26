package raisetech.StudentManagement.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest (StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void 受講生詳細の一覧検索が実行できて空のリストが返ってくる()  throws Exception{
        when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));
      mockMvc.perform(get("/studentList"))
              .andExpect(status().isOk())
                      .andExpect(content().json("[{\"student\":null,\"studentsCourseList\":null}]"));

      verify(service, times(1)).searchStudentList();
    }

    @Test
    void 受講生詳細の受験生で適切な値をIDに数字以外を用いた時に入力チェックに掛かること(){
        Student student = new Student();
        student.setId("0");

        student.setName("吉田正尚");
        student.setKanaName("ヨシダマサタカ");
        student.setNickname("マサタカ");
        student.setEmail("マサタカ@example.com");
        student.setArea("福井");
        student.setSex("男性");


        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(1);
    }






   @Test
    void 受講生詳細の受験生でIDに数字以外を用いた時に入力チェックに掛かること(){
        Student student = new Student();
        student.setId("テストです。");
        student.setName("吉田正尚");
        student.setKanaName("ヨシダマサタカ");
        student.setNickname("マサタカ");
        student.setEmail("マサタカ@example.com");
        student.setArea("福井");
        student.setSex("男性");

       Set<ConstraintViolation<Student>> violations = validator.validate(student);

       assertThat(violations.size()).isEqualTo(2);
       assertThat(violations).extracting("message")
               .containsOnly("数字のみ入力するようにしてください");
   }




}