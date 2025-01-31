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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void 受講生詳細の一覧検査が実行できて空のリストが返ってくること() throws Exception {
        mockMvc.perform(get("/studentList"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).searchStudentList();
    }

    @Test
    void 受講生詳細の検査が実行できて空で返ってくること() throws Exception {
        String id ="999";
        mockMvc.perform(get("/student/{id}",id))
                .andExpect(status().isOk());

        verify(service, times(1)).searchStudent(id);
    }

    @Test
    void 受講生詳細の新規登録が実行できて登録された受講生のリストが返ってくること() throws Exception {
        mockMvc.perform(post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                  {
                      "student": {
                         "name": "山田太郎",
                         "kanaName": "ヤマダタロウ",
                         "nickname": "タロー",
                         "email": "taro@example.com",
                         "area": "東京",
                         "age": 20,
                         "sex": "男性",
                         "remark": ""
                      },
                      "studentsCourseList": [
                      {
                         "courseName": "Javaコース"
                      }
                      ]
                  }
            """
                        ))
                .andExpect(status().isOk());
        verify(service, times(1)).registerStudent(any());
    }

    @Test
    void 受講生詳細の例外APIが実行できてステータスが400で返ってくること() throws Exception {
        mockMvc.perform(get("/exception"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("このAPIは現在利用できません。古いURLとなっています。"));
    }

    @Test
    void 受講生詳細の更新が実行できて空で返ってくること()
            throws Exception {
        //リクエストデータは適切に構築して入力チェックの検証も兼ねている
        mockMvc.perform(put("/updateStudent").contentType(MediaType.APPLICATION_JSON).content(
                        """
                                    {
                                        "student": {
                                            "id": "1",
                                            "name": "山田太郎",
                                            "kanaName": "ヤマダタロウ",
                                            "nickname": "タロー",
                                            "email": "taro@example.com",
                                            "area": "東京",
                                            "age": 20,
                                            "sex": "男性",
                                            "remark": ""
                                        },
                                        "studentsCourseList": [
                                            {
                                                "id": "1",
                                                "studentId": "1",
                                                "courseName": "Javaコース",
                                                "courseStartAt": "2023-04-01T09:00:00",
                                                "courseEndAt": "2023-07-01T15:00:00"
                                            }
                                        ]
                                    }
                                """
                ))
                .andExpect(status().isOk());
    }

        @Test
        void 受講生詳細の一覧検索が実行できて空のリストが返ってくる ()  throws Exception {
            when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));
            mockMvc.perform(get("/studentList"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("[{\"student\":null,\"studentsCourseList\":null}]"));

            verify(service, times(1)).searchStudentList();
        }

        @Test
        void 受講生詳細の受験生で適切な値をIDに数字以外を用いた時に入力チェックに掛かること () {
            Student student = new Student();
            student.setId("0");

            student.setName("山田太郎");
            student.setKanaName("ヤマダタロウ");
            student.setNickname("タロー");
            student.setEmail("taro@example.com");
            student.setArea("東京");
            student.setSex("男性");


            Set<ConstraintViolation<Student>> violations = validator.validate(student);

            assertThat(violations.size()).isEqualTo(1);
        }

        @Test
        void 受講生詳細の受験生でIDに数字以外を用いた時に入力チェックに掛かること () {
            Student student = new Student();
            student.setId("テストです。");
            student.setName("山田太郎");
            student.setKanaName("ヤマダタロウ");
            student.setNickname("タロー");
            student.setEmail("taro@example.com");
            student.setArea("東京");
            student.setSex("男性");

            Set<ConstraintViolation<Student>> violations = validator.validate(student);

            assertThat(violations.size()).isEqualTo(2);
            assertThat(violations).extracting("message")
                    .contains("電子メールアドレスとして正しい形式にしてください", "数字のみ入力するようにしてください");
        }
    }