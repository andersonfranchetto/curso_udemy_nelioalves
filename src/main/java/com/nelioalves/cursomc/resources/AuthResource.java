package com.nelioalves.cursomc.resources;

import com.nelioalves.cursomc.security.jwt.JWTUtil;
import com.nelioalves.cursomc.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + jwtUtil.generateToken(UserService.authenticated().getUsername()));
        return ResponseEntity.noContent().build();
    }
}
