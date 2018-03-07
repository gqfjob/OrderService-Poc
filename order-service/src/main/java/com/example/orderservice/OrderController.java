package com.example.orderservice;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    RestTemplate template = new RestTemplate();
    private static final String url = "http://localhost:3000/orders/";

    @GetMapping("/all")
    public List<Order> getOrders() {
        ResponseEntity<List<Order>> ordersResponse = template.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Order>>() {});
        List<Order> orders = ordersResponse.getBody();
        return orders;
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable("orderId") final int orderId) {
        ResponseEntity<Order> orderResponse = template.exchange(url+orderId, HttpMethod.GET, null, Order.class);
        Order order = orderResponse.getBody();
        return order;
    }
}
