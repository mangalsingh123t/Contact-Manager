package com.contactmanager.contanctmanager.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.contactmanager.contanctmanager.Entites.User;
import com.contactmanager.contanctmanager.Repositories.UserRepository;


public class CustumUserDetailsImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
     User user =  userRepository.findUserByUserName(username);
         if (user == null) {
            throw new UsernameNotFoundException("User Name is Not Found");
         }
         CustumUserDetails custumUserDetails = new CustumUserDetails(user);
         return custumUserDetails;
    } 
    
}
