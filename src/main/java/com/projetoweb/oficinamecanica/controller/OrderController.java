package com.projetoweb.oficinamecanica.controller;

import com.projetoweb.oficinamecanica.dto.OrderProdutoRequestDto;
import com.projetoweb.oficinamecanica.dto.OrderRequestDto;
import com.projetoweb.oficinamecanica.dto.OrderResponseDto;
import com.projetoweb.oficinamecanica.dto.OrderServicoRequestDto;
import com.projetoweb.oficinamecanica.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll() {
        List<OrderResponseDto> list = orderService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id) {
        OrderResponseDto obj = orderService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> insert(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto response = orderService.insert(orderRequestDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> update(@PathVariable Long id, @Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto response = orderService.update(id, orderRequestDto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ITENS DA ORDER ---

    @PostMapping("/{id}/produtos")
    public ResponseEntity<OrderResponseDto> adicionarProduto(@PathVariable Long id,
                                                             @Valid @RequestBody OrderProdutoRequestDto dto) {
        return ResponseEntity.ok(orderService.adicionarProduto(id, dto));
    }

    @DeleteMapping("/{id}/produtos/{produtoId}")
    public ResponseEntity<OrderResponseDto> removerProduto(@PathVariable Long id,
                                                           @PathVariable Long produtoId) {
        return ResponseEntity.ok(orderService.removerProduto(id, produtoId));
    }

    @PostMapping("/{id}/servicos")
    public ResponseEntity<OrderResponseDto> adicionarServico(@PathVariable Long id,
                                                             @Valid @RequestBody OrderServicoRequestDto dto) {
        return ResponseEntity.ok(orderService.adicionarServico(id, dto));
    }

    @DeleteMapping("/{id}/servicos/{servicoId}")
    public ResponseEntity<OrderResponseDto> removerServico(@PathVariable Long id,
                                                           @PathVariable Long servicoId) {
        return ResponseEntity.ok(orderService.removerServico(id, servicoId));
    }
}
