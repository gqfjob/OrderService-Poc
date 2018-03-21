package com.example.orderservice;

import feign.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EnableResourceServer
@RestController
@RequestMapping("/order")
public class OrderController {
    RestTemplate template = new RestTemplate();
    private static final String url = "http://localhost:3000/orders";

    @Autowired
    private ProductServiceClient productService;

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public List<Order> getOrders() {
        ResponseEntity<List<Order>> ordersResponse = template.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Order>>() {});
        List<Order> orders = ordersResponse.getBody();
        Map<Integer, String> products = productService.getProductDetails();
        orders.forEach(o -> setProducts(products, o));
        return orders;
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable("orderId") final int orderId) {
        ResponseEntity<Order> orderResponse = template.exchange(url+"/"+orderId, HttpMethod.GET, null, Order.class);
        Order order = orderResponse.getBody();
        Map<Integer, String> products = productService.getProductDetails();
        setProducts(products, order);
        return order;
    }

    @GetMapping("/user/{userId}")
    public Order getOrderByUserId(@PathVariable("userId") final String userId) {
        ResponseEntity<Order> orderResponse = template.exchange(url+"?userId="+userId, HttpMethod.GET, null, Order.class);
        Order order = orderResponse.getBody();
        Map<Integer, String> products = productService.getProductDetails();
        setProducts(products, order);
        return order;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestPart("uploadfile") MultipartFile uploadfile) {
        productService.uploadFile(uploadfile);
        return "success";
    }

    @GetMapping(
            value = "/download/{fileName}"
    )
    public ResponseEntity download(@PathVariable("fileName") final String fileName) throws IOException {
        /*ResponseEntity responseEntity = productService.download(fileName);
        ByteArrayResource resource = (ByteArrayResource)responseEntity.getBody();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);*/
        return productService.download(fileName);
    }

    private void setProducts(Map<Integer, String> products, Order o) {
        o.setProducts(o.products.stream()
                .map(Integer::parseInt)
                .map(id -> products.get(id))
                .collect(Collectors.toList()));
    }
}
