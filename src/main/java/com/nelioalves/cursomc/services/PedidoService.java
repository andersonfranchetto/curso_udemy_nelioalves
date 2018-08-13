package com.nelioalves.cursomc.services;

import com.nelioalves.cursomc.domain.ItemPedido;
import com.nelioalves.cursomc.domain.PagamentoComBoleto;
import com.nelioalves.cursomc.domain.Pedido;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.security.domain.User;
import com.nelioalves.cursomc.security.exceptions.AuthorizationException;
import com.nelioalves.cursomc.security.services.UserService;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;
import com.nelioalves.cursomc.services.interfaces.EmailService;
import com.nelioalves.cursomc.services.validators.HasRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Service
public class PedidoService {

    @Value("${spring.profiles.active}")
    private String perfilAtivo;

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    public Pedido find(Integer id){
        Pedido pedido = repository.findOne(id);
        if(pedido == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
                    + ", Tipo: " + Pedido.class.getName());
        }
        System.out.println(pedido);
        return pedido;
    }

    @Transactional
    public Pedido insert(Pedido pedido) {
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

        pagamentoService.insert(pedido.getPagamento());

        for(ItemPedido item : pedido.getItens()) {
            item.setDesconto(0.0);
            item.setProduto(produtoService.find(item.getProduto().getId()));
            item.setPreco(item.getProduto().getPreco());
            item.setPedido(pedido);
        };

        itemPedidoService.insert(pedido.getItens());

        switch (perfilAtivo){
            case "test" :
                emailService.sendOrderConfirmationEmail(pedido);
                break;
            default:
                emailService.sendOrderConfirmationHtmlEmail(pedido);
        }

        return pedido;
    }

    public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        if(UserService.authenticated() == null)
            throw new AuthorizationException("Acesso negado.");

        PageRequest pageRequest = new PageRequest(page, linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return repository.findByCliente(clienteService.find(UserService.authenticated().getId()), pageRequest);
    }
}