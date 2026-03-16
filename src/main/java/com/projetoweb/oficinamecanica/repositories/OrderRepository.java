package com.projetoweb.oficinamecanica.repositories;

import com.projetoweb.oficinamecanica.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Carrega orderServicos e orderProdutos junto com a Order,
    // evitando LazyInitializationException ao calcular total ou listar itens.
    @EntityGraph(attributePaths = {"orderServicos", "orderProdutos"})
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findWithItensById(Long id);

    @EntityGraph(attributePaths = {"orderServicos", "orderProdutos"})
    @Query("SELECT o FROM Order o")
    List<Order> findAllWithItens();
}
