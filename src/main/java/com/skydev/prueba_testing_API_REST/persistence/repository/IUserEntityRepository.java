package com.skydev.prueba_testing_API_REST.persistence.repository;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findAllByName(String name);
    List<UserEntity> findAllByAgeBetween(Integer minAge, Integer maxAge);

}
