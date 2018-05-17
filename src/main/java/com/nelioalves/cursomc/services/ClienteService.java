package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;

    public Cliente find(Integer id){
        Cliente cliente = repository.findOne(id);
        if(cliente == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Cliente.class.getName());
        }
        return cliente;
    }
}
