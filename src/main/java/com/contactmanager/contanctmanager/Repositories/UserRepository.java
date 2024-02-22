package com.contactmanager.contanctmanager.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.contanctmanager.Entites.User;

public interface UserRepository extends JpaRepository<User,String> {
    
    @Query("select u from User u where u.email = :email")
    User findUserByUserName(@Param("email") String email);
    

} 