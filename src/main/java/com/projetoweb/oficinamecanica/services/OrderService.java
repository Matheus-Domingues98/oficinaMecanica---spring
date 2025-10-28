package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.OrderResponseDto;
import com.projetoweb.oficinamecanica.entities.Carro;
import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.entities.Order;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.CarroRepository;
import com.projetoweb.oficinamecanica.repositories.ClienteRepository;
import com.projetoweb.oficinamecanica.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarroRepository carroRepository;

    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order não encontrado com id: " + id));
        return new OrderResponseDto(order);
    }

    public List<OrderResponseDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto insert(OrderResponseDto orderResponseDto) {
        // Buscar cliente do banco
        Cliente cliente = clienteRepository.findById(orderResponseDto.getCliente().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + orderResponseDto.getCliente().getId()));

        Order order = new Order();
        order.setCliente(cliente);
        order.setOrderStatus(orderResponseDto.getStatus());
        
        // Se tiver carro no DTO, buscar e setar
        if (orderResponseDto.getCarro() != null && orderResponseDto.getCarro().getId() != null) {
            Carro carro = carroRepository.findById(orderResponseDto.getCarro().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + orderResponseDto.getCarro().getId()));
            order.setCarro(carro);
        }
        
        order = orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto update(Long id, OrderResponseDto orderResponseDto) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order não encontrado com id: " + id));
        
        updateData(order, orderResponseDto);
        order = orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order não encontrado com id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private void updateData(Order order, OrderResponseDto orderResponseDto) {
        // Buscar cliente do banco se foi alterado
        if (orderResponseDto.getCliente() != null && 
            !order.getCliente().getId().equals(orderResponseDto.getCliente().getId())) {
            Cliente cliente = clienteRepository.findById(orderResponseDto.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + orderResponseDto.getCliente().getId()));
            order.setCliente(cliente);
        }
        
        // Buscar carro do banco se foi alterado
        if (orderResponseDto.getCarro() != null && orderResponseDto.getCarro().getId() != null) {
            if (order.getCarro() == null || !order.getCarro().getId().equals(orderResponseDto.getCarro().getId())) {
                Carro carro = carroRepository.findById(orderResponseDto.getCarro().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + orderResponseDto.getCarro().getId()));
                order.setCarro(carro);
            }
        }
        
        order.setOrderStatus(orderResponseDto.getStatus());
    }

}
