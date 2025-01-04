package com.skydev.prueba_testing_API_REST.service.interfaces;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;

import java.util.List;

public interface IUserEntityService {

    UserEntity getUserByEmail(String email);
    List<UserEntity> getAllUserByName(String name);
    List<UserEntity> getAllUsersByAgeBetween(Integer minAge, Integer maxAge);

}
