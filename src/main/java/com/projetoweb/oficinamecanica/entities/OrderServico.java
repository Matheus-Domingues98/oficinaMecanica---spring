package com.projetoweb.oficinamecanica.entities;
import com.projetoweb.oficinamecanica.entities.pk.OrderServicoPK;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "tb_order_servico")
public class OrderServico {

    @EmbeddedId
    private OrderServicoPK id = new OrderServicoPK();

    private String nome;
    private Double preco;
    private String descricao;
    private Duration duracao;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("servicoId")
    @JoinColumn(name = "servico_id")
    private Servico servico;

    public OrderServico() {
    }

    public OrderServico(Order order, Servico servico, String nome, Double preco, String descricao, Duration duracao) {
        this.order = order;
        this.servico = servico;

        this.id.setOrderId(order.getId());
        this.id.setServicoId(servico.getId());

        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.duracao = duracao;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.id.setServicoId(servico.getId());
    }

    public OrderServicoPK getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Duration getDuracao() {
        return duracao;
    }

    public void setDuracao(Duration duracao) {
        this.duracao = duracao;
    }
}
