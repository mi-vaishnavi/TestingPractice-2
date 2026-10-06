package com.vaishu.testing;

import com.vaishu.testing.Model.Users;
import com.vaishu.testing.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepoTest {
    @Autowired
    UserRepo repo;

    @Test
    void shouldSaveAndReturnUser(){
        Users user = new Users(null, "Sravan", "s@gmail.com", "1234");

        Users savedUser = repo.save(user);

        assertNotNull(savedUser.getId());

        Optional<Users> result = repo.findById(savedUser.getId());

        assertTrue(result.isPresent());
        Users foundUser = result.get();

        assertEquals("Sravan", foundUser.getName());
        assertEquals("s@gmail.com", foundUser.getEmail());
        assertEquals("1234", foundUser.getPassword());
    }

    @Test
    void shouldStartWithEmptyDatabase() {

        long count = repo.count();

        assertEquals(0, count);
    }

    @Test
    void shouldFindByName(){
        Users user = new Users(null, "Sravan", "s@gmail.com", "1234");
        Users savedUser = repo.save(user);

        Optional<Users> result = repo.findByName("Sravan");

        assertTrue(result.isPresent());
        assertEquals("Sravan",result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenUserDoesNotExists(){
        Optional<Users> user = repo.findByName("Unknown");

        assertTrue(user.isEmpty());

    }

    @Test
    void shouldDeleteById(){
        Users user = new Users(null, "Sravan", "s@gmail.com", "1234");
        Users savedUser = repo.save(user);

        repo.deleteById(savedUser.getId());

        Optional<Users> result = repo.findById(savedUser.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDuplicateUserIsSaved(){
        Users user1 = new Users(null, "Sravani", "s@gmail.com", "1234");
        Users user2 = new Users(null, "Sravan", "s@gmail.com", "123456");

        repo.saveAndFlush(user1);

        assertThrows(DataIntegrityViolationException.class, ()->{repo.saveAndFlush(user2);});
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull(){
        Users user = new Users(null, "Vaishnavi", "", "1234");
//        repo.saveAndFlush(user);
        assertThrows(DataIntegrityViolationException.class, ()->{repo.saveAndFlush(user);});
    }
}
