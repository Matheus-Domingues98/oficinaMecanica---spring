package com.projetoweb.oficinamecanica.dto;

import com.projetoweb.oficinamecanica.entities.Order;
import com.projetoweb.oficinamecanica.entities.OrderItem;
import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderResponseDto {

    private Long id;
    
    @NotNull(message = "Status é obrigatório")
    private OrderStatus status;
    
    @NotNull(message = "Cliente é obrigatório")
    private ClienteResponseDto cliente;
    
    private CarroResponseDto carro;
    private BigDecimal total;
    private List<OrderItem> itens;
    private Instant createdAt;
    private Instant updatedAt;

    public  OrderResponseDto() {
    }

    public OrderResponseDto(Order entity) {
        this.id = entity.getId();
        this.status = entity.getOrderStatus();
        this.cliente = new ClienteResponseDto(entity.getCliente());
        this.carro = entity.getCarro() != null ? new CarroResponseDto(entity.getCarro()) : null;
        this.total = entity.getTotal();
        this.itens = entity.getItens();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
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

    public ClienteResponseDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResponseDto cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderItem> getItens() {
        return itens;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CarroResponseDto getCarro() {
        return carro;
    }

    public void setCarro(CarroResponseDto carro) {
        this.carro = carro;
    }
}
