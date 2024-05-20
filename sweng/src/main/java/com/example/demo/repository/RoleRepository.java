package com.example.demo.repository;

import com.example.demo.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

//create a role repository that extends JPA repository
public interface RoleRepository extends JpaRepository<Roles,Long> {

}
