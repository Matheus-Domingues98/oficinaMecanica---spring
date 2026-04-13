package com.projetoweb.oficinamecanica.services;

import com.projetoweb.oficinamecanica.dto.OrderProdutoRequestDto;
import com.projetoweb.oficinamecanica.dto.OrderRequestDto;
import com.projetoweb.oficinamecanica.dto.OrderResponseDto;
import com.projetoweb.oficinamecanica.dto.OrderServicoRequestDto;
import com.projetoweb.oficinamecanica.entities.*;
import com.projetoweb.oficinamecanica.entities.pk.OrderProdutoPK;
import com.projetoweb.oficinamecanica.entities.pk.OrderServicoPK;
import com.projetoweb.oficinamecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.oficinamecanica.entities.enums.OrderStatus;
import com.projetoweb.oficinamecanica.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClienteRepository clienteRepository;
    private final CarroRepository carroRepository;
    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;
    private final OrderProdutoRepository orderProdutoRepository;
    private final OrderServicoRepository orderServicoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepository,
                        ClienteRepository clienteRepository,
                        CarroRepository carroRepository,
                        ProdutoRepository produtoRepository,
                        ServicoRepository servicoRepository,
                        OrderProdutoRepository orderProdutoRepository,
                        OrderServicoRepository orderServicoRepository,
                        PagamentoRepository pagamentoRepository,
                        EmailService emailService) {
        this.orderRepository = orderRepository;
        this.clienteRepository = clienteRepository;
        this.carroRepository = carroRepository;
        this.produtoRepository = produtoRepository;
        this.servicoRepository = servicoRepository;
        this.orderProdutoRepository = orderProdutoRepository;
        this.orderServicoRepository = orderServicoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.emailService = emailService;
    }

    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findWithItensById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order não encontrada com id: " + id));
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
        order.setDataValidade(dto.getDataValidade());

        if (dto.getCarroId() != null) {
            Carro carro = carroRepository.findById(dto.getCarroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Carro não encontrado com id: " + dto.getCarroId()));
            validarCarroPertenceAoCliente(carro, cliente);
            order.setCarro(carro);
        }

        validarDataValidadeParaOrcamento(dto.getStatus(), dto.getDataValidade());
        order = orderRepository.save(order);
        return new OrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto update(Long id, OrderRequestDto dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order não encontrada com id: " + id));

        if (dto.getStatus() == OrderStatus.FINALIZADO || dto.getStatus() == OrderStatus.ENTREGUE) {
            if (!pagamentoRepository.existsByOrderId(id)) {
                throw new IllegalArgumentException(
                        "A OS (id=" + id + ") só pode ser finalizada após o registro de pagamento."
                );
            }
        }

        validarDataValidadeParaOrcamento(dto.getStatus(), dto.getDataValidade());
        updateData(order, dto);
        orderRepository.save(order);

        // Recarrega com itens para calcular total corretamente (email + resposta)
        Order orderAtualizada = orderRepository.findWithItensById(id).orElseThrow();
        emailService.enviarAtualizacaoStatus(
                orderAtualizada.getId(),
                orderAtualizada.getCliente().getNome(),
                orderAtualizada.getCliente().getEmail(),
                dto.getStatus(),
                orderAtualizada.getTotal()
        );

        return new OrderResponseDto(orderAtualizada);
    }

    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order não encontrada com id: " + id);
        }
        orderRepository.deleteById(id);
    }

    // --- ITENS DA ORDER ---

    @Transactional
    public OrderResponseDto adicionarProduto(Long orderId, OrderProdutoRequestDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order não encontrada com id: " + orderId));

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + dto.produtoId()));

        OrderProdutoPK pk = new OrderProdutoPK();
        pk.setOrderId(orderId);
        pk.setProdutoId(dto.produtoId());
        if (orderProdutoRepository.existsById(pk)) {
            throw new IllegalArgumentException("Produto (id=" + dto.produtoId() + ") já está na order (id=" + orderId + ").");
        }

        int estoqueDisponivel = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
        if (estoqueDisponivel < dto.quantidade()) {
            throw new IllegalArgumentException(
                    "Estoque insuficiente para o produto (id=" + dto.produtoId() + "). " +
                    "Disponível: " + estoqueDisponivel + ", solicitado: " + dto.quantidade()
            );
        }
        produto.setQuantidade(estoqueDisponivel - dto.quantidade());
        produtoRepository.save(produto);

        OrderProduto item = new OrderProduto(order, produto, produto.getNome(), produto.getPreco(), dto.quantidade());
        orderProdutoRepository.save(item);

        return new OrderResponseDto(orderRepository.findWithItensById(orderId).orElseThrow());
    }

    @Transactional
    public OrderResponseDto removerProduto(Long orderId, Long produtoId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order não encontrada com id: " + orderId);
        }

        OrderProdutoPK pk = new OrderProdutoPK();
        pk.setOrderId(orderId);
        pk.setProdutoId(produtoId);
        OrderProduto orderProduto = orderProdutoRepository.findById(pk)
                .orElseThrow(() -> new ResourceNotFoundException("Produto (id=" + produtoId + ") não encontrado na order (id=" + orderId + ")."));

        Produto produto = orderProduto.getProduto();
        int estoqueAtual = produto.getQuantidade() != null ? produto.getQuantidade() : 0;
        produto.setQuantidade(estoqueAtual + orderProduto.getQuantidade());
        produtoRepository.save(produto);

        orderProdutoRepository.delete(orderProduto);
        return new OrderResponseDto(orderRepository.findWithItensById(orderId).orElseThrow());
    }

    @Transactional
    public OrderResponseDto adicionarServico(Long orderId, OrderServicoRequestDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order não encontrada com id: " + orderId));

        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + dto.servicoId()));

        OrderServicoPK pk = new OrderServicoPK();
        pk.setOrderId(orderId);
        pk.setServicoId(dto.servicoId());
        if (orderServicoRepository.existsById(pk)) {
            throw new IllegalArgumentException("Serviço (id=" + dto.servicoId() + ") já está na order (id=" + orderId + ").");
        }

        OrderServico item = new OrderServico(order, servico, servico.getNome(), servico.getPreco(), servico.getDescricao(), servico.getDuracao());
        orderServicoRepository.save(item);

        return new OrderResponseDto(orderRepository.findWithItensById(orderId).orElseThrow());
    }

    @Transactional
    public OrderResponseDto removerServico(Long orderId, Long servicoId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order não encontrada com id: " + orderId);
        }

        OrderServicoPK pk = new OrderServicoPK();
        pk.setOrderId(orderId);
        pk.setServicoId(servicoId);
        if (!orderServicoRepository.existsById(pk)) {
            throw new ResourceNotFoundException("Serviço (id=" + servicoId + ") não encontrado na order (id=" + orderId + ").");
        }

        orderServicoRepository.deleteById(pk);
        return new OrderResponseDto(orderRepository.findWithItensById(orderId).orElseThrow());
    }

    // --- MÉTODOS PRIVADOS ---

    private void updateData(Order order, OrderRequestDto dto) {
        if (dto.getClienteId() != null && !order.getCliente().getId().equals(dto.getClienteId())) {
            Cliente novoCliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));

            // Se a order já tem carro e nenhum novo carroId foi informado, verifica se o carro pertence ao novo cliente
            if (order.getCarro() != null && dto.getCarroId() == null) {
                Carro carroAtual = order.getCarro();
                if (carroAtual.getCliente() == null || !carroAtual.getCliente().getId().equals(novoCliente.getId())) {
                    throw new IllegalArgumentException(
                            "O carro atual (id=" + carroAtual.getId() + ") não pertence ao novo cliente (id=" + novoCliente.getId() + "). " +
                            "Informe um carroId válido para o novo cliente ou omita o carro."
                    );
                }
            }

            order.setCliente(novoCliente);
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
        order.setDataValidade(dto.getDataValidade());
    }

    private void validarDataValidadeParaOrcamento(OrderStatus status, Instant dataValidade) {
        if (status == OrderStatus.AGUARDANDO_APROVACAO && dataValidade == null) {
            throw new IllegalArgumentException(
                    "dataValidade é obrigatória ao gerar um orçamento (status AGUARDANDO_APROVACAO).");
        }
    }

    private void validarCarroPertenceAoCliente(Carro carro, Cliente cliente) {
        if (carro.getCliente() == null || !carro.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException(
                    "O carro (id=" + carro.getId() + ") não pertence ao cliente (id=" + cliente.getId() + ")."
            );
        }
    }
}