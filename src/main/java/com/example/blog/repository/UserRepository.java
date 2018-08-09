package com.example.blog.repository;


import com.example.blog.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    // implementacja poleceń do DB
    User findOneByEmail(String email);
    // adnotacja Query - native SQL

}
