package com.projetoweb.oficinamecanica.dto;

import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.entities.Order;
import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;

import javax.swing.text.html.parser.Entity;
import java.util.List;

public class OrderResponseDto {

    private Long id;
    private OrderStatus status;
    private Cliente cliente; // Idealmente, você usaria um ClienteDTO aqui também
    private Double total;
    private List<Object> itens;

    public  OrderResponseDto() {
    }

    public OrderResponseDto(Order entity) {
        this.id = entity.getId();
        this.status = entity.getOrderStatus();
        this.cliente = entity.getCliente();
        this.total = entity.getTotal();
        this.itens = entity.getItens();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<Object> getItens() {
        return itens;
    }


}
