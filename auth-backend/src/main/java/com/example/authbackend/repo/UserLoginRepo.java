package com.example.authbackend.repo;

import com.example.authbackend.model.Userlogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepo extends JpaRepository<Userlogin, Long> {
    Optional<Userlogin> findByEmail(String email);
}

//import com.example.authbackend.model.Userlogin;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface UserLoginRepo extends JpaRepository<Userlogin, Long> {
//}
