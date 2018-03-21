package com.example.productservice;

import feign.Headers;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {
    RestTemplate template = new RestTemplate();
    private static final String url = "http://localhost:3000/products/";
    private static String UPLOADED_FOLDER = "C://Work//poc//OrderService-Poc//product-service//src//main//resources//static//";

    @GetMapping("/all")
    public Map<Integer, String> getProductDetails() {
        ResponseEntity<List<Product>> productsResponse = template.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Product>>() {});
        List<Product> products = productsResponse.getBody();
        Map<Integer, String> map = products.stream().collect(
                Collectors.toMap(Product::getId, Product::getName));
        return map;
    }

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable("productId") final int productId) {
        ResponseEntity<Product> productResponse = template.exchange(url+productId, HttpMethod.GET, null, Product.class);
        Product product = productResponse.getBody();
        return product;
    }

    @RequestMapping(method= RequestMethod.POST, value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart("uploadfile") MultipartFile uploadfile) {

        if (uploadfile.isEmpty()) {
            return "failed";
        }

        try {

            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return "failed";
        }

        return "success";

    }

    @GetMapping(value = "/download/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity download(@PathVariable("fileName") String fileName) throws IOException {
        File file = new File(UPLOADED_FOLDER+fileName);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue; //next pls
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }

}
