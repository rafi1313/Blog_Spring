package com.example.blog.repository;

import com.example.blog.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepisitory extends JpaRepository<Role, Long> {
    //metoda zwracająca obiekt klasy Role dla ROLE_USER
    Role findOneByRoleName(String roleName);
}
