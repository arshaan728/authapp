package com.example.authapp.repo;

import com.example.authapp.model.Admin;
import com.example.authapp.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface Consumerrepo extends JpaRepository<Consumer , Long> {
    @Query("SELECT c FROM Consumer c WHERE c.consumername = :username")
    Optional<Consumer> findByconsumerusername(@Param("username") String username);
}
