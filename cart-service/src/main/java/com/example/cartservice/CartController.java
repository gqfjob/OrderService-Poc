package com.example.cartservice;

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
@RequestMapping("/cart")
public class CartController {
    RestTemplate template = new RestTemplate();
    private static final String url = "http://localhost:3000/carts/";

    @GetMapping("/all")
    public List<Cart> getCarts() {
        ResponseEntity<List<Cart>> cartsResponse = template.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Cart>>() {});
        List<Cart> carts = cartsResponse.getBody();
        return carts;
    }

    @GetMapping("/{cartId}")
    public Cart getCart(@PathVariable("cartId") final int cartId) {
        ResponseEntity<Cart> cartResponse = template.exchange(url+cartId, HttpMethod.GET, null, Cart.class);
        Cart cart = cartResponse.getBody();
        return cart;
    }
}
