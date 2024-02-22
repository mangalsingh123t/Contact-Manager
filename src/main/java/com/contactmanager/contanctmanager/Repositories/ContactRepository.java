package com.contactmanager.contanctmanager.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactmanager.contanctmanager.Entites.Contact;
import com.contactmanager.contanctmanager.Entites.User;

import java.util.List;


public interface ContactRepository extends JpaRepository<Contact,String> {
    //  humko user se user ki id se data find karna he user user connect krta he userid ko 
    @Query("select c from Contact c where c.user.uid =:userid")
    Page<Contact> findContactsByUserId(@Param("userid") String userid,Pageable pageable);

    @Query("select c from Contact c where c.cid =:cid")
    Contact findContactsByContactId(@Param("cid") String cid);
    
    @Query("select c from Contact c where lower(c.name) like lower(concat('%',' :name','%'))")
    List<Contact> findByCustumQuery(@Param("name") String name);
    
    // yha pr pertuculer jo user he usse search krna he isliye humne user ko paramter me liya he 
    List<Contact> findByNameContainingAndUser(String name,User user);

}
