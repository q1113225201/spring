package com.sjl.jpa.repository;

import com.sjl.jpa.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
