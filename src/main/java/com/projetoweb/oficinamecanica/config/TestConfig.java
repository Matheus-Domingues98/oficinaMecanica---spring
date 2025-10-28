package com.projetoweb.oficinamecanica.config;

import com.projetoweb.oficinamecanica.entities.*;
import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;
import com.projetoweb.oficinamecanica.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

import static com.projetoweb.oficinamecanica.entities.enums.OrderStatus.AGUARDANDO_APROVACAO;
import static com.projetoweb.oficinamecanica.entities.enums.OrderStatus.FINALIZADO;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private OrderServicoRepository orderServicoRepository;

    @Autowired
    private OrderProdutoRepository orderProdutoRepository;

    @Override
    public void run(String... args) throws Exception {

        Cliente c1 = new Cliente(null, "Matheus", "11922233378", "matheus@gmail.com", "45459090");
        Cliente c2 = new Cliente(null, "Maria", "11933344487", "maria@gmail.com", "45459090");

        clienteRepository.saveAll(Arrays.asList(c1, c2));

        Carro carro1 = new Carro(null, "Fiat Uno", "ABC-1234", "Azul", 2022,"Fiat", c1);
        Carro carro2 = new Carro(null, "Chevrolet Onix", "DEF-5678", "Vermelho", 2021,"Chevrolet", c2);

        carroRepository.saveAll(Arrays.asList(carro1, carro2));

        Order o1 = new Order(null, OrderStatus.RECEBIDO, c1);
        Order o2 = new Order(null, OrderStatus.RECEBIDO, c2);

        orderRepository.saveAll(Arrays.asList(o1, o2));

        Servico s1 = new Servico(null, "Trocar Pneu", 100.0, "Realizar a troca do pneu", Duration.ofHours(2));
        Servico s2 = new Servico(null, "Trocar de Oleo", 100.0, "Realizar a troca do oleo", Duration.ofHours(1));

        servicoRepository.saveAll(Arrays.asList(s1, s2));

        OrderServico os1 = new OrderServico(o1, s1, s1.getNome(), s1.getPreco(), s1.getDescricao(), s1.getDuracao());
        OrderServico os2 = new OrderServico(o2, s2, s2.getNome(), s2.getPreco(), s2.getDescricao(), s2.getDuracao());

        orderServicoRepository.saveAll(Arrays.asList(os1, os2));

        Produto p1 = new Produto(null, "Pneu", 50.0, 4);
        Produto p2 = new Produto(null, "Oleo", 100.0, 1);

        produtoRepository.saveAll(Arrays.asList(p1, p2));

        OrderProduto op1 = new OrderProduto(o1, p1, p1.getNome(), p1.getPreco(), p1.getQuantidade());
        OrderProduto op2 = new OrderProduto(o2, p2, p2.getNome(), p2.getPreco(), p2.getQuantidade());

        orderProdutoRepository.saveAll(Arrays.asList(op1, op2));









    }
}
