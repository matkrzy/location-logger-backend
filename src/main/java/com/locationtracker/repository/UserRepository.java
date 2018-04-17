package com.locationtracker.repository;

import com.locationtracker.enums.UserRole;
import com.locationtracker.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    List<User> findAllByRole(UserRole role);
    User findById(int id);
    Integer removeById(int id);
}
