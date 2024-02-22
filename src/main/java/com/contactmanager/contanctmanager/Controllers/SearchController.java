package com.contactmanager.contanctmanager.Controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.contactmanager.contanctmanager.Entites.Contact;
import com.contactmanager.contanctmanager.Entites.User;
import com.contactmanager.contanctmanager.Repositories.ContactRepository;
import com.contactmanager.contanctmanager.Repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class SearchController {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> getContactsBySearching(@PathVariable("query") String query, Principal principal) {
        String currentuser = principal.getName();
        System.out.println("<<<<<<<<<<<------This is seaching handler for implementing searching : --------------->>>>>>>>");
        User user = userRepository.findUserByUserName(currentuser);
        List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);
        return ResponseEntity.ok(contacts);
    }

    // @GetMapping("/search/{query}")
    // public ResponseEntity<?> getContactsBySearching(@PathVariable("query") String query) {
    //     // String currentuser = principal.getName();
    //     // User user = userRepository.findUserByUserName(currentuser);
    //     List<Contact> contacts = contactRepository.findByCustumQuery(query);
    //     return ResponseEntity.ok(contacts);
    // }

}
