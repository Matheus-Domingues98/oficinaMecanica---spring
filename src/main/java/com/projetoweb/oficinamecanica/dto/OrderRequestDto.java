package com.projetoweb.oficinamecanica.dto;

import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para criação e atualização de Order.
 * Contém apenas os campos que o cliente pode enviar — IDs de referência e status.
 * Campos calculados (total, itens) ficam somente em OrderResponseDto.
 */
public class OrderRequestDto {

    @NotNull(message = "Status é obrigatório")
    private OrderStatus status;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    private Long carroId;

    public OrderRequestDto() {
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getCarroId() {
        return carroId;
    }

    public void setCarroId(Long carroId) {
        this.carroId = carroId;
    }
}