package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.Pagamento;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository repository;

    @Autowired
    PagamentoService pagamentoService;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    ItemPedidoService itemPedidoService;

    @Autowired
    ClienteService clienteService;

    public Pedido find(Integer id){
        Pedido pedido = repository.findOne(id);
        if(pedido == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Pedido.class.getName());
        }
        return pedido;
    }

    @Transactional
    public Pedido insert(Pedido pedido){
        pedido.setId(null);
        pedido.setInstante(new Date());
        pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
        pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE);
        pedido.getPagamento().setPedido(pedido);

        if(pedido.getPagamento() instanceof PagamentoComBoleto) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pedido.getInstante());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            ((PagamentoComBoleto) pedido.getPagamento()).setDataVencimento(calendar.getTime());
        }

        pedido = repository.save(pedido);

        System.out.println(pedido.getItens());

        pagamentoService.insert(pedido.getPagamento());

        for(ItemPedido item : pedido.getItens()) {
            item.setDesconto(0.0);
            item.setProduto(produtoService.find(item.getProduto().getId()));
            item.setPreco(item.getProduto().getPreco());
            item.setPedido(pedido);
        };

        itemPedidoService.insert(pedido.getItens());

        return pedido;
    }
}
