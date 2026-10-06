package com.vaishu.testing.repo;

import com.vaishu.testing.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByName(String name);
//    void DeleteById(Integer id);
}
