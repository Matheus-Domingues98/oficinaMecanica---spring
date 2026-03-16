package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.OrderRequestDto;
import com.projetoweb.oficinamecanica.dto.OrderResponseDto;
import com.projetoweb.oficinamecanica.entities.Carro;
import com.projetoweb.oficinamecanica.entities.Cliente;
import com.projetoweb.oficinamecanica.entities.Order;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.repositories.CarroRepository;
import com.projetoweb.oficinamecanica.repositories.ClienteRepository;
import com.projetoweb.oficinamecanica.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClienteRepository clienteRepository;
    private final CarroRepository carroRepository;

    public OrderService(OrderRepository orderRepository,
                        ClienteRepository clienteRepository,
                        CarroRepository carroRepository) {
        this.orderRepository = orderRepository;
        this.clienteRepository = clienteRepository;
        this.carroRepository = carroRepository;
    }

    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findWithItensById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order não encontrado com id: " + id));
        return new OrderResponseDto(order);
    }

    public List<OrderResponseDto> findAll() {
        return orderRepository.findAllWithItens()
                .stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto insert(OrderRequestDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));

        Order order = new Order();
        order.setCliente(cliente);
        order.setOrderStatus(dto.getStatus());

        if (dto.getCarroId() != null) {
            Carro carro = carroRepository.findById(dto.getCarroId())
                .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + dto.getCarroId()));
            validarCarroPertenceAoCliente(carro, cliente);
            order.setCarro(carro);
        }

        order = orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto update(Long id, OrderRequestDto dto) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order não encontrado com id: " + id));

        updateData(order, dto);
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

    private void updateData(Order order, OrderRequestDto dto) {
        if (dto.getClienteId() != null && !order.getCliente().getId().equals(dto.getClienteId())) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));
            order.setCliente(cliente);
        }

        if (dto.getCarroId() != null) {
            if (order.getCarro() == null || !order.getCarro().getId().equals(dto.getCarroId())) {
                Carro carro = carroRepository.findById(dto.getCarroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + dto.getCarroId()));
                validarCarroPertenceAoCliente(carro, order.getCliente());
                order.setCarro(carro);
            }
        }

        order.setOrderStatus(dto.getStatus());
    }

    private void validarCarroPertenceAoCliente(Carro carro, Cliente cliente) {
        if (carro.getCliente() == null || !carro.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException(
                "O carro (id=" + carro.getId() + ") não pertence ao cliente (id=" + cliente.getId() + ")."
            );
        }
    }

}
