package com.projetoweb.oficinamecanica.entities;

import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer orderStatus;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    @OneToMany(mappedBy = "order")
    private Set<OrderServico> orderServicos = new HashSet<>();

    @OneToMany(mappedBy = "order")
    private Set<OrderProduto> orderProdutos = new HashSet<>();

    public Order() {
    }

    public Order(Long id, OrderStatus orderStatus, Cliente cliente) {
        this.id = id;
        setOrderStatus(orderStatus);
        this.cliente = cliente;
    }

    public Order(Long id, OrderStatus orderStatus, Cliente cliente, Carro carro) {
        this.id = id;
        setOrderStatus(orderStatus);
        this.cliente = cliente;
        this.carro = carro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        // Converte o código Integer armazenado de volta para o enum
        return OrderStatus.valueOf(orderStatus);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        // Armazena o código Integer no banco de dados
        if (orderStatus != null) {
            this.orderStatus = orderStatus.getCode();
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public Set<OrderServico> getOrderServicos() {
        return orderServicos;
    }

    public Set<OrderProduto> getOrderProdutos() {
        return orderProdutos;
    }

    public List<Object> getItens() {
        return Stream.concat(
                orderProdutos.stream(),
                orderServicos.stream()
        ).collect(Collectors.toList());
    }

    // --- MÉTODOS DE NEGÓCIO ---

    /**
     * Calcula o valor total do pedido.
     * @return Valor total do pedido.
     */
    public Double getTotal() {
         double totalServico = orderServicos.stream().mapToDouble(OrderServico::getPreco).sum();
         double totalProduto = orderProdutos.stream().mapToDouble(OrderProduto::getSubTotal).sum();
         return totalServico + totalProduto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }




}
