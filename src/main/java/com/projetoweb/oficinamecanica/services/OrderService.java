package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.OrderResponseDto;
import com.projetoweb.oficinamecanica.entities.Order;
import com.projetoweb.oficinamecanica.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderResponseDto findById(Long id) {
        // Buscar entidade no banco de dados
        Optional<Order> order = orderRepository.findById(id);

        // Verificar se a entidade foi encontrada
        if (order.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        // Converter entidade para DTO
        OrderResponseDto orderDto = new OrderResponseDto(order.get());

        return orderDto;
    }

    public List<OrderResponseDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    public OrderResponseDto insert(OrderResponseDto orderResponseDto) {
        Order order = new Order();
        order.setCliente(orderResponseDto.getCliente());
        order.setOrderStatus(orderResponseDto.getStatus());
        order.setTotal(orderResponseDto.getTotal());
        order = orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    public OrderResponseDto update(Long id, OrderResponseDto orderResponseDto) {
        Order order = orderRepository.findById(id).get();
        updateData(order, orderResponseDto);
        order = orderRepository.save(order);

        return new OrderResponseDto(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public void updateData(Order order, OrderResponseDto orderResponseDto) {
        order.setCliente(orderResponseDto.getCliente());
        order.setOrderStatus(orderResponseDto.getStatus());
        order.setTotal(orderResponseDto.getTotal());
    }

}
