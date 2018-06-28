package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import com.nelioalves.cursomc.services.interfaces.EmailService;
import com.nelioalves.cursomc.services.validators.util.GeneratePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void sendNewPassword(String email){

        Cliente cliente = clienteRepository.findByEmail(email);

        if(cliente == null){
            throw new ObjectNotFoundException("Usuário não localizado");
        }
        String newPassword = GeneratePassword.generatePassword();
        cliente.setSenha(bCryptPasswordEncoder.encode(newPassword));

        clienteRepository.save(cliente);

        emailService.sendNewPasswordEmail(cliente, newPassword);
    }

}
