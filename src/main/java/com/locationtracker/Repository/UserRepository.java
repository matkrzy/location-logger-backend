package com.locationtracker.Repository;

import com.locationtracker.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
}
