package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.Pagamento;
import com.nelioalves.cursomc.repositories.ItemPedidoRepository;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ItemPedidoService {

    @Autowired
    ItemPedidoRepository repository;


    public ItemPedido find(Integer id){
        ItemPedido itemPedido = repository.findOne(id);
        if(itemPedido == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + ItemPedidoService.class.getName());
        }
        return itemPedido;
    }

    public void insert(Set<ItemPedido> itens) {
        repository.save(itens);
    }
}
