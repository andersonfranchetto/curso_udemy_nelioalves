package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository repository;

    public Categoria find(Integer id){
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

    public Categoria update(Categoria categoria){
        find(categoria.getId());//VERIFICA SE ID EXISTE.
        return repository.save(categoria);
    }

    public void delete(Integer id){
        find(id);
        try {
            repository.delete(id);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir uma categoria relacionada a um ou mais produtos.");
        }
    }

    public List<Categoria> findAll(){
        return repository.findAll();
    }
}
