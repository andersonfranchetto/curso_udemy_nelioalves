package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.Pagamento;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository repository;

    @Autowired
    CategoriaService categoriaService;

    public Pagamento find(Integer id){
        Pagamento pagamento = repository.findOne(id);
        if(pagamento == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Pagamento.class.getName());
        }
        return pagamento;
    }

    public Pagamento insert(Pagamento pagamento){
        return repository.save(pagamento);
    }
}
