package com.nelioalves.cursomc.services.profile;

import com.nelioalves.cursomc.domain.*;
import com.nelioalves.cursomc.domain.enums.EstadoPagamento;
import com.nelioalves.cursomc.domain.enums.PerfilCliente;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Service
public class DBService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    EstadoRepository estadoRepository;

    @Autowired
    CidadeRepository cidadeRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    ItemPedidoRepository itemPedidoRepository;

    public void instantiateTestDatabase() throws ParseException{

        Categoria cat1 = new Categoria(null, "Informática");
        Categoria cat2 = new Categoria(null, "Escritório");
        Categoria cat3 = new Categoria(null, "Cama Mesa e Banho");
        Categoria cat4 = new Categoria(null, "Eletrônicos");
        Categoria cat5 = new Categoria(null, "Jardinagem");
        Categoria cat6 = new Categoria(null, "Decoração");
        Categoria cat7 = new Categoria(null, "Perfumaria");

        Produto p1 = new Produto(null, "Computador", 2000.00);
        Produto p2 = new Produto(null, "Impressora", 1200.00);
        Produto p3 = new Produto(null, "Mouse", 30.00);
        Produto p4 = new Produto(null, "Mesa de Escritório", 300.00);
        Produto p5 = new Produto(null, "Toalha", 50.00);
        Produto p6 = new Produto(null, "Colcha", 200.00);
        Produto p7 = new Produto(null, "TV Smart", 1200.00);
        Produto p8 = new Produto(null, "Roçadeira", 800.00);
        Produto p9 = new Produto(null, "Abajour", 100.00);
        Produto p10 = new Produto(null, "Pendente", 180.00);
        Produto p11 = new Produto(null, "Shampoo", 90.00);

        cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
        cat2.getProdutos().addAll(Arrays.asList(p2, p4));
        cat3.getProdutos().addAll(Arrays.asList(p5, p6));
        cat4.getProdutos().addAll(Arrays.asList(p1, p2, p3, p7));
        cat5.getProdutos().addAll(Arrays.asList(p8));
        cat6.getProdutos().addAll(Arrays.asList(p9, p10));
        cat7.getProdutos().addAll(Arrays.asList(p11));

        p1.getCategorias().addAll(Arrays.asList(cat1));
        p2.getCategorias().addAll(Arrays.asList(cat1, cat2, cat4));
        p3.getCategorias().addAll(Arrays.asList(cat1, cat4));
        p4.getCategorias().addAll(Arrays.asList(cat2));
        p5.getCategorias().addAll(Arrays.asList(cat3));
        p6.getCategorias().addAll(Arrays.asList(cat3));
        p7.getCategorias().addAll(Arrays.asList(cat4));
        p8.getCategorias().addAll(Arrays.asList(cat5));
        p9.getCategorias().addAll(Arrays.asList(cat6));
        p10.getCategorias().addAll(Arrays.asList(cat6));
        p11.getCategorias().addAll(Arrays.asList(cat7));

        Estado est1 = new Estado(null, "São Paulo");
        Estado est2 = new Estado(null, "Minas Gerais");

        Cidade cid1 = new Cidade(null, "Fernandópolis", est1);
        Cidade cid2 = new Cidade(null, "Uberlândia", est2);
        Cidade cid3 = new Cidade(null, "Campinas", est1);

        est1.getCidades().addAll(Arrays.asList(cid2));
        est1.getCidades().addAll(Arrays.asList(cid1, cid3));

        Cliente cli1 = new Cliente(null, "Maria Silva", "anderson.franchetto@gmail.com", "555656565", TipoCliente.PESSOAFISICA, bCryptPasswordEncoder.encode("123"));
        Cliente cli2 = new Cliente(null, "João Silva", "anderson.benatti@gmail.com", "556448845", TipoCliente.PESSOAFISICA, bCryptPasswordEncoder.encode("123"));

        cli1.getTelefones().addAll(Arrays.asList("33332222", "22223333"));
        cli2.getTelefones().addAll(Arrays.asList("34432222", "25523333"));
        cli2.addPerfil(PerfilCliente.ADMIN);

        Endereco end1 = new Endereco(null, "Rua Flores", "300", "Apto 3", "Jardim", "38383838", cli1, cid2);
        Endereco end2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38383838", cli1, cid1);
        Endereco end3 = new Endereco(null, "Avenida Matos", "1875", "", "Centro", "38383838", cli2, cid1);
        cli1.getEnderecos().addAll(Arrays.asList(end1, end2));
        cli1.getEnderecos().addAll(Arrays.asList(end3));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, end1);
        Pedido ped2 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, end2);

        Pagamento pag1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
        ped1.setPagamento(pag1);
        Pagamento pag2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 14:50"), null);
        ped2.setPagamento(pag2);

        cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

        categoriaRepository.save(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
        produtoRepository.save(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11));

        estadoRepository.save(Arrays.asList(est1, est2));
        cidadeRepository.save(Arrays.asList(cid1, cid2, cid3));

        clienteRepository.save(Arrays.asList(cli1, cli2));
        enderecoRepository.save(Arrays.asList(end1, end2, end3));

        pedidoRepository.save(Arrays.asList(ped1, ped2));
        pagamentoRepository.save(Arrays.asList(pag1, pag2));

        ItemPedido ip1 = new ItemPedido(ped1, p1, 0.0, 1, 2000.0);
        ItemPedido ip2 = new ItemPedido(ped1, p3, 0.0, 2, 30.0);
        ItemPedido ip3 = new ItemPedido(ped2, p2, 100.0, 1, 1200.0);

        ped1.getItens().addAll(Arrays.asList(ip1, ip2));
        ped2.getItens().addAll(Arrays.asList(ip3));

        p1.getItens().addAll(Arrays.asList(ip1));
        p2.getItens().addAll(Arrays.asList(ip3));
        p3.getItens().addAll(Arrays.asList(ip2));

        itemPedidoRepository.save(Arrays.asList(ip1, ip2, ip3));
    }
}
