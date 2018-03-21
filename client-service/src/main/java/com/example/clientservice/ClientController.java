/*
package com.example.clientservice;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ClientController {
    RestTemplate template = new RestTemplate();

    @PostMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") final int productId) {
        ResponseEntity<?> productResponse = template.exchange(, HttpMethod.POST, null, Product.class);
        Product product = productResponse.getBody();
        return product;
    }
}
*/
