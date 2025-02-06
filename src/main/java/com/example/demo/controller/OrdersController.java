package com.example.demo.controller;

import com.example.demo.service.OrdersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrdersController {

    private final OrdersService ordersService;
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("placeOrder/{cartId}")
    public ResponseEntity<String> placeOrder(@PathVariable Long cartId) {
        return ordersService.placeOrder(cartId);

    }
}
