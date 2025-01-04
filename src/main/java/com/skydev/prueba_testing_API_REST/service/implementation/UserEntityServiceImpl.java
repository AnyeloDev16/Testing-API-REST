package com.skydev.prueba_testing_API_REST.service.implementation;

import com.skydev.prueba_testing_API_REST.persistence.model.UserEntity;
import com.skydev.prueba_testing_API_REST.persistence.repository.IUserEntityRepository;
import com.skydev.prueba_testing_API_REST.service.exception.ResourceNotFoundException;
import com.skydev.prueba_testing_API_REST.service.interfaces.IUserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEntityServiceImpl implements IUserEntityService {

    private final IUserEntityRepository userEntityRepository;

    @Override
    public UserEntity getUserByEmail(String email) {
        return userEntityRepository.findByEmail(email).orElseThrow( () ->
                new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<UserEntity> getAllUserByName(String name) {
        return userEntityRepository.findAllByName(name);
    }

    @Override
    public List<UserEntity> getAllUsersByAgeBetween(Integer minAge, Integer maxAge) {
        return userEntityRepository.findAllByAgeBetween(minAge, maxAge);
    }
}
