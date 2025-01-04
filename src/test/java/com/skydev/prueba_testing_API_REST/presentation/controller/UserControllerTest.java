package com.skydev.prueba_testing_API_REST.presentation.controller;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;
import com.skydev.prueba_testing_API_REST.service.exception.ResourceNotFoundException;
import com.skydev.prueba_testing_API_REST.service.implementation.UserEntityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@Slf4j
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserEntityServiceImpl userService;

    @Nested
    class TestFindUserByEmail{

        @Test
        @DisplayName("Find success")
        void testFindSuccess() throws Exception {

            //Given

            String emailSearch = "test@skydev.com";

            UserEntity userFind = UserEntity.builder()
                    .name("test")
                    .email("test@skydev.com")
                    .age(21)
                    .build();

            given(userService.getUserByEmail(emailSearch)).willReturn(userFind);

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/email/{email}", emailSearch));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value(userFind.getName()))
                    .andExpect(jsonPath("$.email").value(userFind.getEmail()))
                    .andExpect(jsonPath("$.age").value(userFind.getAge()));

        }

        @Test
        @DisplayName("Find failure")
        void testFindFailure() throws Exception {

            //Given

            String emailSearch = "test@skydev.com";

            String msgError  = "User not found";

            given(userService.getUserByEmail(emailSearch)).willThrow(new ResourceNotFoundException(msgError));

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/email/{email}", emailSearch));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                    .andExpect(content().string(msgError));

        }

    }

    @Nested
    class TestFindAllUserByName{

        @Test
        @DisplayName("Find no user")
        void testFindNoUser() throws Exception {

            //Given

            String nameSearch = "Anyelo";

            given(userService.getAllUserByName(nameSearch)).willReturn(Collections.emptyList());

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/name/{name}", nameSearch));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

        }

        @Test
        @DisplayName("Find one user")
        void testFindOneUser() throws Exception {

            //Given

            String nameSearch = "Anyelo";

            UserEntity userFind = UserEntity.builder()
                    .name("Anyelo")
                    .email("test@skydev.com")
                    .age(21)
                    .build();

            given(userService.getAllUserByName(nameSearch)).willReturn(List.of(userFind));

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/name/{name}", nameSearch));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$[0].name").value(userFind.getName()))
                    .andExpect(jsonPath("$[0].email").value(userFind.getEmail()))
                    .andExpect(jsonPath("$[0].age").value(userFind.getAge()));

        }

        @Test
        @DisplayName("Find multiple user")
        void testFindMultipleUser() throws Exception {

            //Given

            String nameSearch = "Anyelo";

            UserEntity userFind1 = UserEntity.builder()
                    .name("Anyelo")
                    .email("test@skydev.com")
                    .age(21)
                    .build();

            UserEntity userFind2 = UserEntity.builder()
                    .name("Anyelo")
                    .email("ocm@skydev.com")
                    .age(23)
                    .build();

            UserEntity userFind3 = UserEntity.builder()
                    .name("Anyelo")
                    .email("camp@skydev.com")
                    .age(25)
                    .build();


            given(userService.getAllUserByName(nameSearch)).willReturn(List.of(userFind1, userFind2, userFind3));

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/name/{name}", nameSearch));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.length()").value(3))
                    .andExpect(jsonPath("$[0].name").value(userFind1.getName()))
                    .andExpect(jsonPath("$[0].email").value(userFind1.getEmail()))
                    .andExpect(jsonPath("$[0].age").value(userFind1.getAge()))
                    .andExpect(jsonPath("$[1].name").value(userFind2.getName()))
                    .andExpect(jsonPath("$[1].email").value(userFind2.getEmail()))
                    .andExpect(jsonPath("$[1].age").value(userFind2.getAge()))
                    .andExpect(jsonPath("$[2].name").value(userFind3.getName()))
                    .andExpect(jsonPath("$[2].email").value(userFind3.getEmail()))
                    .andExpect(jsonPath("$[2].age").value(userFind3.getAge()));

        }

    }

    @Nested
    class TestFindAllUserByAgeBetween{

        @Test
        @DisplayName("Find no user")
        void testFindNoUser() throws Exception {

            //Given

            Integer ageMin = 21;
            Integer ageMax = 23;

            given(userService.getAllUsersByAgeBetween(ageMin, ageMax)).willReturn(Collections.emptyList());

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/age/{minAge}/{maxAge}", ageMin, ageMax));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

        }

        @Test
        @DisplayName("Find one user")
        void testFindOneUser() throws Exception {

            //Given

            Integer ageMin = 21;
            Integer ageMax = 23;

            UserEntity userFind = UserEntity.builder()
                    .name("Anyelo")
                    .email("test@skydev.com")
                    .age(22)
                    .build();

            given(userService.getAllUsersByAgeBetween(ageMin, ageMax)).willReturn(List.of(userFind));

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/age/{minAge}/{maxAge}", ageMin, ageMax));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$[0].name").value(userFind.getName()))
                    .andExpect(jsonPath("$[0].email").value(userFind.getEmail()))
                    .andExpect(jsonPath("$[0].age").value(userFind.getAge()));

        }

        @Test
        @DisplayName("Find multiple user")
        void testFindMultipleUser() throws Exception {

            //Given

            Integer ageMin = 21;
            Integer ageMax = 23;

            UserEntity userFind1 = UserEntity.builder()
                    .name("Anyelo")
                    .email("test@skydev.com")
                    .age(21)
                    .build();

            UserEntity userFind2 = UserEntity.builder()
                    .name("Anyelo")
                    .email("ocm@skydev.com")
                    .age(22)
                    .build();

            UserEntity userFind3 = UserEntity.builder()
                    .name("Anyelo")
                    .email("camp@skydev.com")
                    .age(23)
                    .build();


            given(userService.getAllUsersByAgeBetween(ageMin, ageMax)).willReturn(List.of(userFind1, userFind2, userFind3));

            //When

            log.info("GET request");
            ResultActions resultActions = mockMvc.perform(get("/api/users/age/{minAge}/{maxAge}", ageMin, ageMax));

            //Then

            log.info("Verification of the result");
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(jsonPath("$.length()").value(3))
                    .andExpect(jsonPath("$[0].name").value(userFind1.getName()))
                    .andExpect(jsonPath("$[0].email").value(userFind1.getEmail()))
                    .andExpect(jsonPath("$[0].age").value(userFind1.getAge()))
                    .andExpect(jsonPath("$[1].name").value(userFind2.getName()))
                    .andExpect(jsonPath("$[1].email").value(userFind2.getEmail()))
                    .andExpect(jsonPath("$[1].age").value(userFind2.getAge()))
                    .andExpect(jsonPath("$[2].name").value(userFind3.getName()))
                    .andExpect(jsonPath("$[2].email").value(userFind3.getEmail()))
                    .andExpect(jsonPath("$[2].age").value(userFind3.getAge()));

        }

    }

}