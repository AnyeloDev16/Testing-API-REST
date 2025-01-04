package com.skydev.prueba_testing_API_REST.service.implementation;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;
import com.skydev.prueba_testing_API_REST.persistence.repository.IUserEntityRepository;
import com.skydev.prueba_testing_API_REST.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Slf4j
class UserEntityServiceImplTest {

    @Mock
    private IUserEntityRepository userEntityRepository;

    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    @Nested
    class TestFindByEmail{

        @Test
        @DisplayName("Find success")
        void testFindSuccess(){

            //Given

            String emailSearch = "anyelo@gmail.com";

            UserEntity userSearch = UserEntity.builder()
                    .name("Anyelo")
                    .email(emailSearch)
                    .age(20)
                    .build();

            given(userEntityRepository.findByEmail(emailSearch)).willReturn(Optional.of(userSearch));

            //When

            log.info("Searching for all user NAME: {}", emailSearch);
            UserEntity userResult = userEntityService.getUserByEmail(emailSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(userResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findByEmail(emailSearch),
                    () -> assertEquals(emailSearch, userResult.getEmail())
            );

        }

        @Test
        @DisplayName("Find failure")
        void testFindFailure(){

            //Given

            String emailSearch = "jose@gmail.com";

            given(userEntityRepository.findByEmail(emailSearch)).willReturn(Optional.empty());

            //When

            log.info("Getting exception when not finding user");
            ResourceNotFoundException rnfe = assertThrows(ResourceNotFoundException.class,
                    () -> userEntityService.getUserByEmail(emailSearch));
            //Then

            log.info("Check exception obtained");
            assertNotNull(rnfe);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findByEmail(emailSearch),
                    () -> assertInstanceOf(ResourceNotFoundException.class, rnfe),
                    () -> assertEquals(rnfe.getMessage(), "User not found")
            );

        }

    }

    @Nested
    class TestFindAllByName{

        @Test
        @DisplayName("Find no users")
        void testFindNoUser(){

            //Given

            String nameSearch = "Jose";

            Integer cantUsersExpect = 0;

            given(userEntityRepository.findAllByName(nameSearch)).willReturn(Collections.emptyList());

            //When

            log.info("Searching for all user NAME: {}", nameSearch);
            List<UserEntity> listUserResult = userEntityService.getAllUserByName(nameSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findAllByName(nameSearch),
                    () -> assertEquals(cantUsersExpect, listUserResult.size())
            );

        }

        @Test
        @DisplayName("Find one user")
        void testFindOneUser(){

            //Given

            String nameSearch = "Jose";

            Integer cantUsersExpect = 1;

            given(userEntityRepository.findAllByName(nameSearch))
                    .willReturn(List.of(UserEntity.builder()
                                    .name("Jose")
                                    .email("jose@gmail.com")
                                    .age(20)
                            .build()));

            //When

            log.info("Searching for all user NAME: {}", nameSearch);
            List<UserEntity> listUserResult = userEntityService.getAllUserByName(nameSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findAllByName(nameSearch),
                    () -> assertEquals(cantUsersExpect, listUserResult.size()),
                    () -> assertEquals(nameSearch, listUserResult.getFirst().getName())
            );

        }

        @Test
        @DisplayName("Find multiple user")
        void testFindMultipleUser(){

            //Given

            String nameSearch = "Anyelo";

            Integer cantUsersExpect = 3;

            UserEntity user1 = UserEntity.builder()
                    .name("Anyelo")
                    .email("anelo@gmail.com")
                    .age(19)
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .name("Anyelo")
                    .email("yelo@gmail.com")
                    .age(20)
                    .build();

            UserEntity user3 = UserEntity.builder()
                    .name("Anyelo")
                    .email("any@gmail.com")
                    .age(21)
                    .build();

            given(userEntityRepository.findAllByName(nameSearch))
                    .willReturn(List.of(user1, user2, user3));

            //When

            log.info("Searching for all user NAME: {}", nameSearch);
            List<UserEntity> listUserResult = userEntityService.getAllUserByName(nameSearch);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findAllByName(nameSearch),
                    () -> assertEquals(cantUsersExpect, listUserResult.size())
            );

        }

    }

    @Nested
    class TestFindAllByAgeBetween{

        @Test
        @DisplayName("Find no user")
        void testFindNoUser(){

            //Given

            Integer minAge = 20;
            Integer maxAge = 27;

            Integer cantUsersExpect = 0;

            given(userEntityRepository.findAllByAgeBetween(minAge, maxAge)).willReturn(Collections.emptyList());

            //When

            log.info("Searching for all user AGE: {} - {}", minAge, maxAge);
            List<UserEntity> listUserResult = userEntityService.getAllUsersByAgeBetween(minAge, maxAge);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findAllByAgeBetween(minAge, maxAge),
                    () -> assertEquals(cantUsersExpect, listUserResult.size())
            );

        }

        @Test
        @DisplayName("Find one user")
        void testFindOneUser(){

            //Given

            Integer minAge = 20;
            Integer maxAge = 27;

            Integer cantUsersExpect = 1;

            UserEntity userEntity = UserEntity.builder()
                    .name("Anyelo")
                    .email("anelo@gmail.com")
                    .age(21)
                    .build();

            given(userEntityRepository.findAllByAgeBetween(minAge, maxAge)).willReturn(List.of(userEntity));

            //When

            log.info("Searching for all user AGE: {} - {}", minAge, maxAge);
            List<UserEntity> listUserResult = userEntityService.getAllUsersByAgeBetween(minAge, maxAge);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findAllByAgeBetween(minAge, maxAge),
                    () -> assertEquals(cantUsersExpect, listUserResult.size()),
                    () -> assertTrue((minAge <= listUserResult.getFirst().getAge()) && (maxAge >= listUserResult.getFirst().getAge()))
            );

        }

        @Test
        @DisplayName("Find multiple user")
        void testFindMultipleUser(){

            //Given

            Integer minAge = 20;
            Integer maxAge = 27;

            Integer cantUsersExpect = 3;

            UserEntity user1 = UserEntity.builder()
                    .name("Anyelo")
                    .email("anelo@gmail.com")
                    .age(21)
                    .build();

            UserEntity user2 = UserEntity.builder()
                    .name("Jose")
                    .email("jose@gmail.com")
                    .age(22)
                    .build();

            UserEntity user3 = UserEntity.builder()
                    .name("Matias")
                    .email("matias@gmail.com")
                    .age(24)
                    .build();

            given(userEntityRepository.findAllByAgeBetween(minAge, maxAge))
                    .willReturn(List.of(user1, user2, user3));

            //When

            log.info("Searching for all user AGE: {} - {}", minAge, maxAge);
            List<UserEntity> listUserResult = userEntityService.getAllUsersByAgeBetween(minAge, maxAge);

            //Then

            log.info("Checking the data with the expected data");
            assertNotNull(listUserResult);

            assertAll(
                    () -> verify(userEntityRepository, times(1)).findAllByAgeBetween(minAge, maxAge),
                    () -> assertEquals(cantUsersExpect, listUserResult.size())
            );

        }

    }

}