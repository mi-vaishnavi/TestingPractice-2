package com.vaishu.testing.Services;

import com.vaishu.testing.Exceptions.UserAlreadyExistsException;
import com.vaishu.testing.Model.Users;
import com.vaishu.testing.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepo repo;

    public Users register(Users user) {
        Users savedUser = repo.save(user);
        if(user.getEmail().equals("existing@email.com")) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        return savedUser;
    }
}
