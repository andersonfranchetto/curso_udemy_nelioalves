package com.nelioalves.cursomc.security.services;

import com.nelioalves.cursomc.security.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public static User authenticated(){
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
    }
}
