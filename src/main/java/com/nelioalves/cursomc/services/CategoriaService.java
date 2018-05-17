package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository repository;

    public Categoria buscar(Integer id){
        Categoria categoria = repository.findOne(id);
        if(categoria == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Categoria.class.getName());
        }
        return categoria;
    }

    public Categoria insert(Categoria categoria){
        return repository.save(categoria);
    }
}
