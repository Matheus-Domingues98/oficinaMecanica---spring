package com.projetoweb.oficinamecanica.repositories;

import com.projetoweb.oficinamecanica.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
