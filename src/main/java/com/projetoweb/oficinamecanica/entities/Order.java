package com.projetoweb.oficinamecanica.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "tb_order")
@EntityListeners(AuditingEntityListener.class)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer orderStatus;

    @Column(name = "data_validade")
    private Instant dataValidade;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    @OneToMany(mappedBy = "order")
    @JsonIgnore
    private Set<OrderServico> orderServicos = new HashSet<>();

    @OneToMany(mappedBy = "order")
    @JsonIgnore
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
        return orderStatus != null ? OrderStatus.fromCode(orderStatus) : null;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Instant dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<OrderServico> getOrderServicos() {
        return orderServicos;
    }

    public Set<OrderProduto> getOrderProdutos() {
        return orderProdutos;
    }

    public List<OrderItem> getItens() {
        return Stream.concat(
                orderProdutos.stream(),
                orderServicos.stream()
        ).collect(Collectors.toList());
    }

    // --- MÉTODOS DE NEGÓCIO ---

    /**
     * Calcula o valor total do pedido usando BigDecimal para evitar
     * erros de ponto flutuante em valores monetários.
     */
    public BigDecimal getTotal() {
        BigDecimal totalServico = orderServicos.stream()
                .map(OrderServico::getPreco)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProduto = orderProdutos.stream()
                .map(OrderProduto::getSubTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalServico.add(totalProduto);
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
