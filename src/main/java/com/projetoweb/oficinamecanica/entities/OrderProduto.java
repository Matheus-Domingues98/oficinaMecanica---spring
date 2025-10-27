package com.projetoweb.oficinamecanica.entities;

import com.projetoweb.oficinamecanica.entities.pk.OrderProdutoPK;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_order_produto")
public class OrderProduto implements Serializable {

    @EmbeddedId
    private OrderProdutoPK id = new OrderProdutoPK();

    private String nome;
    private Double preco;
    private Integer quantidade;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public  OrderProduto() {
    }

    public OrderProduto(Order order, Produto produto, String nome, Double preco, Integer quantidade) {
       this.order = order;
       this.produto = produto;

       this.id.setOrderId(order.getId());
       this.id.setProdutoId(produto.getId());

       this.nome = nome;
       this.preco = preco;
       this.quantidade = quantidade;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.id.setProdutoId(produto.getId());
    }

    public OrderProdutoPK getId() {
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getSubTotal() {
        return preco * quantidade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduto that = (OrderProduto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
