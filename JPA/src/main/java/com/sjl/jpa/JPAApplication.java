package com.sjl.jpa;

import com.sjl.jpa.model.Order;
import com.sjl.jpa.model.Product;
import com.sjl.jpa.repository.OrderRepository;
import com.sjl.jpa.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

@EnableJpaRepositories
@Slf4j
@SpringBootApplication
public class JPAApplication implements ApplicationRunner {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public static void main(String[] args) {
        SpringApplication.run(JPAApplication.class, args);

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initData();
    }

    private void initData() {
        Product clothes = Product.builder()
                .name("衣服")
                .price(Money.of(CurrencyUnit.of("CNY"), 100.0))
                .build();
        productRepository.save(clothes);
        Product pants = Product.builder()
                .name("裤子")
                .price(Money.of(CurrencyUnit.of("CNY"), 200.0))
                .build();
        productRepository.save(pants);
        Iterator<Product> productIterator = productRepository.findAll().iterator();
        while (productIterator.hasNext()) {
            log.info("Product:{}", productIterator.next());
        }

        Order order = Order.builder()
                .products(Collections.singletonList(clothes))
                .state(0)
                .build();
        orderRepository.save(order);
        log.info("Order:{}", order);
        order = Order.builder()
                .products(Arrays.asList(clothes,pants))
                .state(0)
                .build();
        orderRepository.save(order);
        Iterator<Order> orderIterator = orderRepository.findAll().iterator();
        while (orderIterator.hasNext()) {
            log.info("Order:{}", orderIterator.next());
        }
        order.setState(1);
        orderRepository.save(order);
        log.info("update-----------------");
        orderIterator = orderRepository.findAll().iterator();
        while (orderIterator.hasNext()) {
            log.info("Order:{}", orderIterator.next());
        }

    }
}
