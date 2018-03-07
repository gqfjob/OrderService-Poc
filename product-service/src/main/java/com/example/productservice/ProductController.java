package com.example.productservice;

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
@RequestMapping("/product")
public class ProductController {
    RestTemplate template = new RestTemplate();
    private static final String url = "http://localhost:3000/products/";

    @GetMapping("/all")
    public List<Product> getProducts() {
        ResponseEntity<List<Product>> productsResponse = template.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {});
        List<Product> products = productsResponse.getBody();
        return products;
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable("productId") final int productId) {
        ResponseEntity<Product> productResponse = template.exchange(url+productId, HttpMethod.GET, null, Product.class);
        Product product = productResponse.getBody();
        return product;
    }
}
