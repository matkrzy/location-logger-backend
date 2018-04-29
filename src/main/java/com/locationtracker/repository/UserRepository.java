package com.locationtracker.repository;

import com.locationtracker.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);

    User findById(int id);

}
