package com.example.authapp.repo;

import com.example.authapp.model.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface Adminrepo extends JpaRepository<Admin, String> {
   @Query("SELECT a FROM Admin a WHERE a.adminusername = :username")
   Optional<Admin> findByadminusername(@Param("username") String username);
}
