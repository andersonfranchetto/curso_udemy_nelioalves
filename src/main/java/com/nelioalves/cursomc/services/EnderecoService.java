package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository repository;

    public Endereco find(Integer id){
        Endereco endereco = repository.findOne(id);
        if(endereco == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Endereco.class.getName());
        }
        return endereco;
    }

    public Endereco insert(Endereco endereco){
        return repository.save(endereco);
    }

    public Endereco update(Endereco endereco){
        find(endereco.getId());//VERIFICA SE ID EXISTE.
        return repository.save(endereco);
    }

    public void delete(Integer id){
        find(id);
        try {
            repository.delete(id);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir um endereco relacionada a um ou mais pedidos.");
        }
    }

    public List<Endereco> findAll(){
        return repository.findAll();
    }

    public Page<Endereco> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = new PageRequest(page, linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return repository.findAll(pageRequest);
    }
}
