package com.davo.backend.repository;

import com.davo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
    
    @Query(value = "SELECT * FROM User WHERE "
            + "CONCAT(User.full_name, User.email)"
            + "LIKE %?1% ", nativeQuery = true)
    public List<User> searchUser(String keyword);
}
