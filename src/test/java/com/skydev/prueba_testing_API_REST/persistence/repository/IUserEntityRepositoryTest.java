package com.skydev.prueba_testing_API_REST.persistence.repository;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IUserEntityRepositoryTest {

    @Autowired
    private IUserEntityRepository repo;

    @BeforeAll
    void setupOnce() {

        UserEntity user1 = UserEntity.builder()
                .name("Anyelo")
                .age(20)
                .email("anyelo@gmail.com")
                .build();

        UserEntity user2 = UserEntity.builder()
                .name("Isaac")
                .age(21)
                .email("isaac@gmail.com")
                .build();

        UserEntity user3 = UserEntity.builder()
                .name("Anyelo")
                .age(19)
                .email("pego@gmail.com")
                .build();

        repo.save(user1);
        repo.save(user2);
        repo.save(user3);

    }

    @Nested
    class TestFindUserByEmail{

        @Test
        @DisplayName("Find success")
        void testFindSuccess(){

            //Given

            String emailSearch = "isaac@gmail.com";
            Long idExpected = 2L;
            String nameExpected = "Isaac";
            Integer ageExpected = 21;

            //When

            log.info("Searching for user EMAIL: {}", emailSearch);
            UserEntity userResult = repo.findByEmail(emailSearch).orElse(null);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(userResult);

            assertAll(
                    () -> assertEquals(idExpected, userResult.getId()),
                    () -> assertEquals(nameExpected, userResult.getName()),
                    () -> assertEquals(emailSearch, userResult.getEmail()),
                    () -> assertEquals(ageExpected, userResult.getAge())
            );

        }

        @Test
        @DisplayName("Find failure")
        void testFindFailure(){

            //Given

            String emailSearch = "jose@gmail.com";

            //When

            log.info("Searching for user EMAIL: {}", emailSearch);
            UserEntity userResult = repo.findByEmail(emailSearch).orElse(null);

            //Then

            log.info("Verifying that the user does not exist");

            assertAll(
                    () -> assertNull(userResult)
            );

        }

    }

    @Nested
    class TestFindAllByName{

        @Test
        @DisplayName("Find no user")
        void testFindNoUser(){

            //Given

            String nameSearch = "Jose";

            //When

            log.info("Searching for all user NAME: {}", nameSearch);
            List<UserEntity> listUserResult = repo.findAllByName(nameSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> assertTrue(listUserResult.isEmpty())
            );

        }

        @Test
        @DisplayName("Find one user")
        void testFindOneUser(){

            //Given

            String nameSearch = "Isaac";
            Integer cantUser = 1;
            Long idExpected = 2L;
            String emailExpected = "isaac@gmail.com";
            Integer ageExpected = 21;


            //When

            log.info("Searching for all user NAME: {}", nameSearch);
            List<UserEntity> listUserResult = repo.findAllByName(nameSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> assertFalse(listUserResult.isEmpty()),
                    () -> assertEquals(cantUser, listUserResult.size()),
                    () -> assertEquals(idExpected, listUserResult.getFirst().getId()),
                    () -> assertEquals(nameSearch, listUserResult.getFirst().getName()),
                    () -> assertEquals(emailExpected, listUserResult.getFirst().getEmail()),
                    () -> assertEquals(ageExpected, listUserResult.getFirst().getAge())
            );

        }

        @Test
        @DisplayName("Find multiple user")
        void testFindMultipleUser(){

            //Given

            String nameSearch = "Anyelo";
            Integer cantUser = 2;

            //When

            log.info("Searching for all user NAME: {}", nameSearch);
            List<UserEntity> listUserResult = repo.findAllByName(nameSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> assertFalse(listUserResult.isEmpty()),
                    () -> assertEquals(cantUser, listUserResult.size())
            );

        }

    }

    @Nested
    class TestFindAllByAgeBetween{

        @Test
        @DisplayName("Find no user")
        void testFindNoUser(){

            //Given

            Integer minAge = 23;
            Integer maxAge = 27;

            //When

            log.info("Searching for all user AGE: {} - {}", minAge, maxAge);
            List<UserEntity> listUserResult = repo.findAllByAgeBetween(minAge, maxAge);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> assertTrue(listUserResult.isEmpty())
            );

        }

        @Test
        @DisplayName("Find one user")
        void testFindOneUser(){

            //Given

            Integer minAge = 21;
            Integer maxAge = 27;
            Integer cantUser = 1;

            Long idExpected = 2L;
            String nameExpected = "Isaac";
            String emailExpected = "isaac@gmail.com";
            Integer ageExpected = 21;


            //When

            log.info("Searching for all user AGE: {} - {}", minAge, maxAge);
            List<UserEntity> listUserResult = repo.findAllByAgeBetween(minAge, maxAge);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> assertFalse(listUserResult.isEmpty()),
                    () -> assertEquals(cantUser, listUserResult.size()),
                    () -> assertEquals(idExpected, listUserResult.getFirst().getId()),
                    () -> assertEquals(nameExpected, listUserResult.getFirst().getName()),
                    () -> assertEquals(emailExpected, listUserResult.getFirst().getEmail()),
                    () -> assertEquals(ageExpected, listUserResult.getFirst().getAge())
            );

        }

        @Test
        @DisplayName("Find multiple user")
        void testFindMultipleUser(){

            //Given

            Integer minAge = 19;
            Integer maxAge = 21;

            Integer cantUser = 3;

            //When

            log.info("Searching for all user AGE: {} - {}", minAge, maxAge);
            List<UserEntity> listUserResult = repo.findAllByAgeBetween(minAge, maxAge);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> assertFalse(listUserResult.isEmpty()),
                    () -> assertEquals(cantUser, listUserResult.size())
            );

        }

    }

}